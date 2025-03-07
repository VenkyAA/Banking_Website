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

import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    // Logger to log messages
    private static final Logger logger = Logger.getLogger(LoanServiceImpl.class.getName());

    // Autowire the LoanRepository to interact with the database
    @Autowired
    private LoanRepository loanRepository;

    // Autowire the AccountLClient to interact with the Account Service
    @Autowired
    private AccountLClient accountClient;

    // Create a new loan
    @Override
    public LoanDTO createLoan(@Valid LoanDTO loanDTO) {
        // Get the account details from the Account Service
        AccountDTO account = accountClient.getAccountById(loanDTO.getId());
        if (account == null) {
            // If the account is not found, throw an AccountNotFoundException
            throw new AccountNotFoundException("Account not found");
        }

        // Use the provided CIBIL score and determine the interest rate
        int cibilScore = loanDTO.getCibilScore();
        double interestRate = determineInterestRate(cibilScore);

        // Map LoanDTO to Loan entity
        Loan loan = LoanMapper.mapToLoan(loanDTO);
        loan.setInterestRate(interestRate);
        loan.setAmountToBeRepaid(calculateAmountToBeRepaid(loan.getPrincipalAmount(), interestRate, loan.getNumberOfYears()));
        loan.setAmountPaid(0);

        // Save the loan to the database
        Loan savedLoan = loanRepository.save(loan);
        return LoanMapper.mapToLoanDTO(savedLoan);
    }

    // Get loans by account ID
    @Override
    public List<LoanDTO> getLoansById(long id) {
        // Get the list of loans by account ID
        List<Loan> loans = loanRepository.findById(id);
        return loans.stream()
                .map(LoanMapper::mapToLoanDTO)
                .collect(Collectors.toList());
    }

    // Repay a loan
    @Override
    public LoanDTO repayLoan(@Valid LoanDTO loanDTO) {
        // Get the loan details by loan ID
        Loan loan = loanRepository.findByLoanId(loanDTO.getLoanId())
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with loanId: " + loanDTO.getLoanId()));

        // Get the account details from the Account Service
        AccountDTO account = accountClient.getAccountById(loanDTO.getId());
        double repayedAmount = loanDTO.getAmountPaid();

        // Update the loan details
        loan.setAmountToBeRepaid(loan.getAmountToBeRepaid() - repayedAmount);
        loan.setAmountPaid(loan.getAmountPaid() + repayedAmount);

        // Save the updated loan to the database
        Loan updatedLoan = loanRepository.save(loan);
        return LoanMapper.mapToLoanDTO(updatedLoan);
    }

    // Get loan by loan ID
    @Override
    public LoanDTO getLoanByLoanId(long loanId) {
        // Get the loan details by loan ID
        Loan loan = loanRepository.findByLoanId(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        return LoanMapper.mapToLoanDTO(loan);
    }

    // Delete a loan by loan ID
    @Override
    public void deleteLoan(long loanId) {
        // Get the loan details by loan ID
        Loan loan = loanRepository.findByLoanId(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        // Delete the loan from the database
        loanRepository.delete(loan);
    }

    // Calculate the amount to be repaid using simple interest formula: A = P(1 + rt)
    private double calculateAmountToBeRepaid(double principal, double interestRate, int numberOfYears) {
        return principal * (1 + (interestRate * numberOfYears));
    }

    // Determine the interest rate based on the CIBIL score
    private double determineInterestRate(int cibilScore) {
        if (cibilScore > 850) {
            return 5.0;
        } else if (cibilScore >= 760) {
            return 6.41;
        } else if (cibilScore >= 700) {
            return 6.63;
        } else if (cibilScore >= 680) {
            return 6.81;
        } else if (cibilScore >= 660) {
            return 7.02;
        } else if (cibilScore >= 640) {
            return 7.45;
        } else if (cibilScore >= 620) {
            return 8.0;
        } else {
            return 10.0;
        }
    }

	@Override
	public List<LoanDTO> getAllLoans() {
		List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(LoanMapper::mapToLoanDTO).collect(Collectors.toList());
	}

	@Override
	public void deleteLoansByAccountId(long id) {
	    List<Loan> loans = loanRepository.findById(id);
	    if (loans.isEmpty()) {
	        throw new LoanNotFoundException("No loans found for account ID: " + id);
	    }
	    loanRepository.deleteAll(loans);
	    logger.info("Deleted loans for account ID: " + id);
	}

}
