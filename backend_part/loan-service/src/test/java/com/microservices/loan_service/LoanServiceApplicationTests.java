package com.microservices.loan_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.microservices.loan_service.dto.LoanDTO;
import com.microservices.loan_service.dto.AccountDTO;
import com.microservices.loan_service.entity.Loan;
import com.microservices.loan_service.exception.AccountNotFoundException;
import com.microservices.loan_service.exception.LoanNotFoundException;
import com.microservices.loan_service.mapper.LoanMapper;
import com.microservices.loan_service.repository.LoanRepository;
import com.microservices.loan_service.feign.AccountLClient;
import com.microservices.loan_service.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoanServiceApplicationTests {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AccountLClient accountClient;

    @InjectMocks
    private LoanServiceImpl loanService;

    private LoanDTO loanDTO;
    private Loan loan;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        loanDTO = new LoanDTO();
        loanDTO.setLoanId(1);
        loanDTO.setId(1);
        loanDTO.setPrincipalAmount(1000);
        loanDTO.setNumberOfYears(2);
        loanDTO.setInterestRate(5);
        loanDTO.setAmountToBeRepaid(1100);
        loanDTO.setAmountPaid(0);
        loanDTO.setLoanType("Personal");
        loanDTO.setCibilScore(700);

        loan = LoanMapper.mapToLoan(loanDTO);

        accountDTO = new AccountDTO();
        accountDTO.setId(1);
        accountDTO.setAccountHolder("John Doe");
        accountDTO.setBalance(5000);
    }

    @Test
    void testCreateLoan() {
        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO createdLoan = loanService.createLoan(loanDTO);

        assertNotNull(createdLoan);
        assertEquals(loanDTO.getId(), createdLoan.getId());
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoan_AccountNotFound() {
        when(accountClient.getAccountById(anyLong())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            loanService.createLoan(loanDTO);
        });

        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(loanRepository, times(0)).save(any(Loan.class));
    }

    @Test
    void testGetLoansById() {
        when(loanRepository.findById(anyLong())).thenReturn(Collections.singletonList(loan));

        List<LoanDTO> loans = loanService.getLoansById(1L);

        assertNotNull(loans);
        assertEquals(1, loans.size());
        verify(loanRepository, times(1)).findById(anyLong());
    }

    @Test
    void testRepayLoan() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.of(loan));
        when(accountClient.getAccountById(anyLong())).thenReturn(accountDTO);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanDTO repaidLoan = loanService.repayLoan(loanDTO);

        assertNotNull(repaidLoan);
        verify(loanRepository, times(1)).findByLoanId(anyLong());
        verify(accountClient, times(1)).getAccountById(anyLong());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testRepayLoan_LoanNotFound() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> {
            loanService.repayLoan(loanDTO);
        });

        verify(loanRepository, times(1)).findByLoanId(anyLong());
        verify(accountClient, times(0)).getAccountById(anyLong());
        verify(loanRepository, times(0)).save(any(Loan.class));
    }

    @Test
    void testGetLoanByLoanId() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.of(loan));

        LoanDTO foundLoan = loanService.getLoanByLoanId(1L);

        assertNotNull(foundLoan);
        assertEquals(loanDTO.getLoanId(), foundLoan.getLoanId());
        verify(loanRepository, times(1)).findByLoanId(anyLong());
    }

    @Test
    void testGetLoanByLoanId_LoanNotFound() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> {
            loanService.getLoanByLoanId(1L);
        });

        verify(loanRepository, times(1)).findByLoanId(anyLong());
    }

    @Test
    void testDeleteLoan() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).delete(any(Loan.class));

        loanService.deleteLoan(1L);

        verify(loanRepository, times(1)).findByLoanId(anyLong());
        verify(loanRepository, times(1)).delete(any(Loan.class));
    }

    @Test
    void testDeleteLoan_LoanNotFound() {
        when(loanRepository.findByLoanId(anyLong())).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> {
            loanService.deleteLoan(1L);
        });

        verify(loanRepository, times(1)).findByLoanId(anyLong());
        verify(loanRepository, times(0)).delete(any(Loan.class));
    }
}
