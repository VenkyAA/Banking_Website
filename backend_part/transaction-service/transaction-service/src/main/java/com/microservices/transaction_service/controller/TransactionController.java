package com.microservices.transaction_service.controller;

import com.microservices.transaction_service.dto.TransactionDTO;
import com.microservices.transaction_service.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> createTransferTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransferTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsById(@PathVariable long id) {
        List<TransactionDTO> transactions = transactionService.getTransactionsById(id);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionByTransactionId(@PathVariable long transactionId) {
        TransactionDTO transactionDTO = transactionService.getTransactionByTransactionId(transactionId);
        return ResponseEntity.ok(transactionDTO);
    }
    


    @GetMapping("/account/{id}/balance")
    public ResponseEntity<Double> getAccountBalance(@PathVariable long id) {
        double balance = transactionService.getAccountBalance(id);
        return ResponseEntity.ok(balance);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }
}

