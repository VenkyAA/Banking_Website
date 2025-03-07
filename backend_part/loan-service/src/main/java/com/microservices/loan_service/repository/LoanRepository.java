package com.microservices.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservices.loan_service.entity.Loan;

import feign.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	@Query("SELECT l FROM Loan l WHERE l.loanId = :loanId")
	Optional<Loan> findByLoanId(@Param("loanId") long loanId);
	
	@Query("SELECT l FROM Loan l WHERE l.id = :id")
	List<Loan> findById(@Param("id") long id);
}
