package com.microservices.loan_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loans")
@Entity
public class Loan {

    @Id
    private long loanId;  // New attribute for loanId

    private long id;
    private double principalAmount;
    private int numberOfYears;
    private double interestRate;
    private double amountToBeRepaid;
    private double amountPaid;
    private String loanType;
}

