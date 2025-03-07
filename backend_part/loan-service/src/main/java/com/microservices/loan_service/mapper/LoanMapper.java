package com.microservices.loan_service.mapper;

import com.microservices.loan_service.dto.LoanDTO;
import com.microservices.loan_service.entity.Loan;

// This class provides methods to map between Loan and LoanDTO objects
public class LoanMapper {

    // Map LoanDTO to Loan entity
    public static Loan mapToLoan(LoanDTO loanDTO) {
        Loan loan = new Loan();
        loan.setLoanId(loanDTO.getLoanId()); // Set loanId
        loan.setId(loanDTO.getId()); // Set account ID
        loan.setPrincipalAmount(loanDTO.getPrincipalAmount()); // Set principal amount
        loan.setNumberOfYears(loanDTO.getNumberOfYears()); // Set number of years
        loan.setInterestRate(loanDTO.getInterestRate()); // Set interest rate
        loan.setAmountToBeRepaid(loanDTO.getAmountToBeRepaid()); // Set amount to be repaid
        loan.setAmountPaid(loanDTO.getAmountPaid()); // Set amount paid
        loan.setLoanType(loanDTO.getLoanType()); // Set loan type
        loan.setCibilScore(loanDTO.getCibilScore()); // Set CIBIL score
        return loan;
    }

    // Map Loan entity to LoanDTO
    public static LoanDTO mapToLoanDTO(Loan loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(loan.getLoanId()); // Set loanId
        loanDTO.setId(loan.getId()); // Set account ID
        loanDTO.setPrincipalAmount(loan.getPrincipalAmount()); // Set principal amount
        loanDTO.setNumberOfYears(loan.getNumberOfYears()); // Set number of years
        loanDTO.setInterestRate(loan.getInterestRate()); // Set interest rate
        loanDTO.setAmountToBeRepaid(loan.getAmountToBeRepaid()); // Set amount to be repaid
        loanDTO.setAmountPaid(loan.getAmountPaid()); // Set amount paid
        loanDTO.setLoanType(loan.getLoanType()); // Set loan type
        loanDTO.setCibilScore(loan.getCibilScore()); // Set CIBIL score
        return loanDTO;
    }
}
