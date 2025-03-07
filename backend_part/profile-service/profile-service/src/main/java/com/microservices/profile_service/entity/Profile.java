package com.microservices.profile_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
@Entity
public class Profile {

    @Id
    private long id;

    @Column(name = "account_holder_name")
    private String accountHolder;

    @Column(name = "govt_id")
    private String govtId;

    private String employment;
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_id")
    private String emailId;

    
}

