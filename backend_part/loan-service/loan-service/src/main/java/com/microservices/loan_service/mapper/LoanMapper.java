package com.microservices.loan_service.mapper;

import com.microservices.loan_service.dto.LoanDTO;
import com.microservices.loan_service.entity.Loan;

public class LoanMapper {

    public static Loan mapToLoan(LoanDTO loanDTO) {
        Loan loan = new Loan();
        loan.setLoanId(loanDTO.getLoanId());  // Map loanId
        loan.setId(loanDTO.getId());
        loan.setPrincipalAmount(loanDTO.getPrincipalAmount());
        loan.setNumberOfYears(loanDTO.getNumberOfYears());
        loan.setInterestRate(loanDTO.getInterestRate());
        loan.setAmountToBeRepaid(loanDTO.getAmountToBeRepaid());
        loan.setAmountPaid(loanDTO.getAmountPaid());
        loan.setLoanType(loanDTO.getLoanType());
        return loan;
    }

    public static LoanDTO mapToLoanDTO(Loan loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(loan.getLoanId());  // Map loanId
        loanDTO.setId(loan.getId());
        loanDTO.setPrincipalAmount(loan.getPrincipalAmount());
        loanDTO.setNumberOfYears(loan.getNumberOfYears());
        loanDTO.setInterestRate(loan.getInterestRate());
        loanDTO.setAmountToBeRepaid(loan.getAmountToBeRepaid());
        loanDTO.setAmountPaid(loan.getAmountPaid());
        loanDTO.setLoanType(loan.getLoanType());
        return loanDTO;
    }
}

