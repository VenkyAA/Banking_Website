package com.microservices.account_service.controller;

import com.microservices.account_service.dto.AccountCreationRequest;
import com.microservices.account_service.dto.AccountDTO;
import com.microservices.account_service.dto.BalanceUpdateDTO;
import com.microservices.account_service.service.AccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
//@CrossOrigin(origins = "http://localhost:3001")
public class AccountController {

    
    private AccountService accountService;
    
    

    public AccountController(AccountService accountService) {
		super();
		this.accountService = accountService;
	}

	@PostMapping("/create")
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
