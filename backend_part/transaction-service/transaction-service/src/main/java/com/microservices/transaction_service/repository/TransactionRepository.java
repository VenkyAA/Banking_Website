package com.microservices.transaction_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservices.transaction_service.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@Query("SELECT t FROM Transaction t WHERE t.id = :id")
	List<Transaction> findById(@Param("id") long id);
	
	@Query("SELECT t FROM Transaction t WHERE t.transactionId = :transactionId")
	Optional<Transaction> findByTransactionId(@Param("transactionId") long transactionId);
}

