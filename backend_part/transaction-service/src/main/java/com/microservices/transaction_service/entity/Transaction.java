package com.microservices.transaction_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
@Entity
public class Transaction {

    @Id
    private long transactionId; // This is the generated value

    private long id; // This is the account ID
    private String type;
    private double amount;
    private Date date;
    private Long targetAccountId;
}


