package com.microservices.account_service.controller;

import com.microservices.account_service.dto.AccountCreationRequest;
import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.dto.AmountRequest;
import com.microservices.account_service.dto.BalanceUpdateDTO;
import com.microservices.account_service.service.AccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    
    private AccountService accountService;
    
    

    public AccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	@PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody @Valid AccountCreationRequest accountCreationDTO) {
        try {
            return new ResponseEntity<>(accountService.createAccount(accountCreationDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable long id) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable long id, @RequestBody @Valid AmountRequest amountRequest) {
    	double amount = amountRequest.getAmount();
    	AccountDTO accountDTO = accountService.deposit(id, amount);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable long id, @RequestBody @Valid AmountRequest amountRequest) {
        double amount = amountRequest.getAmount();
        AccountDTO accountDTO = accountService.withdraw(id, amount);
        return ResponseEntity.ok(accountDTO);
    }


    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account is deleted successfully!");
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<Void> updateAccountBalance(@PathVariable("id") long id, @RequestBody BalanceUpdateDTO balanceUpdateDTO) {
        accountService.updateAccountBalance(id, balanceUpdateDTO.getAmount());
        return ResponseEntity.noContent().build();
    }
    

}
