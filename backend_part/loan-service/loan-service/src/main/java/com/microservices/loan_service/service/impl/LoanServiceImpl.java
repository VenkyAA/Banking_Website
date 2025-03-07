package com.microservices.loan_service.service.impl;

import com.microservices.loan_service.dto.LoanDTO;
import com.microservices.loan_service.dto.AccountDTO;
import com.microservices.loan_service.entity.Loan;
import com.microservices.loan_service.exception.AccountNotFoundException;
import com.microservices.loan_service.exception.LoanNotFoundException;
import com.microservices.loan_service.mapper.LoanMapper;
import com.microservices.loan_service.repository.LoanRepository;
import com.microservices.loan_service.feign.AccountLClient;
import com.microservices.loan_service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountLClient accountClient;

    @Override
    public LoanDTO createLoan(LoanDTO loanDTO) {
        AccountDTO account = accountClient.getAccountById(loanDTO.getId());
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }

        Loan loan = LoanMapper.mapToLoan(loanDTO);
        loan.setAmountToBeRepaid(calculateAmountToBeRepaid(loanDTO));
        loan.setAmountPaid(0);
        Loan savedLoan = loanRepository.save(loan);
        return LoanMapper.mapToLoanDTO(savedLoan);
    }

    @Override
    public List<LoanDTO> getLoansById(long id) {
        List<Loan> loans = loanRepository.findLoansById(id);
        return loans.stream()
                .map(LoanMapper::mapToLoanDTO)
                .collect(Collectors.toList());
    }



    @Override
    public LoanDTO repayLoan(LoanDTO loanDTO) {
        Loan loan = loanRepository.findByLoanId(loanDTO.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with loanId: " + loanDTO.getLoanId()));

        AccountDTO account = accountClient.getAccountById(loanDTO.getId());
        double repaymentAmount = loanDTO.getAmountToBeRepaid();
        
        loan.setAmountToBeRepaid(loan.getAmountToBeRepaid() - repaymentAmount);
        loan.setAmountPaid(loan.getAmountPaid() + repaymentAmount);
        Loan updatedLoan = loanRepository.save(loan);

        return LoanMapper.mapToLoanDTO(updatedLoan);
    }

    @Override
    public LoanDTO getLoanByLoanId(long loanId) {
        Loan loan = loanRepository.findByLoanId(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        return LoanMapper.mapToLoanDTO(loan);
    }

    @Override
    public void deleteLoan(long loanId) {
        Loan loan = loanRepository.findByLoanId(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        loanRepository.delete(loan);
    }

    private double calculateAmountToBeRepaid(LoanDTO loanDTO) {
        double principal = loanDTO.getPrincipalAmount();
        double interestRate = loanDTO.getInterestRate() / 100;
        int numberOfYears = loanDTO.getNumberOfYears();

        // Simple Interest Calculation: A = P(1 + rt)
        return principal * (1 + (interestRate * numberOfYears));
    }
}
