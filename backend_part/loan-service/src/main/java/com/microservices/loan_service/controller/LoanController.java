package com.microservices.loan_service.controller;

import com.microservices.loan_service.dto.LoanDTO;
import com.microservices.loan_service.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
//@CrossOrigin(origins = "http://localhost:3001")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        try {
            return new ResponseEntity<>(loanService.createLoan(loanDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<LoanDTO>> getLoansById(@PathVariable long id) {
        List<LoanDTO> loans = loanService.getLoansById(id);
        return ResponseEntity.ok(loans);
    }

    @PutMapping("/repay/{loanId}")
    public ResponseEntity<LoanDTO> repayLoan(@PathVariable long loanId, @RequestBody LoanDTO loanDTO) {
        return new ResponseEntity<>(loanService.repayLoan(loanDTO), HttpStatus.OK);
    }

    // New endpoint to get loan by loanId
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<LoanDTO> getLoanByLoanId(@PathVariable long loanId) {
        LoanDTO loanDTO = loanService.getLoanByLoanId(loanId);
        return ResponseEntity.ok(loanDTO);
    }
    
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // New endpoint to delete a loan by loanId
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteLoan(@PathVariable long loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteLoansByAccountId(@PathVariable long id) {
        loanService.deleteLoansByAccountId(id);
        return ResponseEntity.ok().build();
    }

}

