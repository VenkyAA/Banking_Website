package com.microservices.loan_service.service;

import com.microservices.loan_service.dto.LoanDTO;
import java.util.List;

public interface LoanService {
    LoanDTO createLoan(LoanDTO loanDTO);
    
    List<LoanDTO> getLoansById(long id);
    
    LoanDTO repayLoan(LoanDTO loanDTO);
    
    LoanDTO getLoanByLoanId(long loanId);
    
    void deleteLoan(long loanId);
}

