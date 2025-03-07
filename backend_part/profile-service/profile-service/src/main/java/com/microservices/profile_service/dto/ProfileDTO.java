package com.microservices.profile_service.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class ProfileDTO {
    private long id;

    @NotEmpty(message = "Account holder cannot be empty")
    private String accountHolder;

    @NotEmpty(message = "Government ID cannot be empty")
    private String govtId;

    @NotEmpty(message = "Employment cannot be empty")
    private String employment;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotEmpty(message = "Phone number cannot be empty")
    @Size(min = 10, max = 12, message = "Phone number should be between 10 and 15 characters")
    private String phoneNumber;

    @NotEmpty(message = "Email ID cannot be empty")
    @Email(message = "Email ID should be valid")
    private String emailId;

    public ProfileDTO(long id, String accountHolder, String govtId, String employment, String address, String phoneNumber, String emailId) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.govtId = govtId;
        this.employment = employment;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
    }
}
