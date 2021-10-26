package com.codecool.bankaccountstarter.model.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountDto {
    @NotBlank
    @Size(min=2, max=256)
    protected String ownerName;

    @NotBlank
    @Size(min=4, max=128)
    protected String password;

    public CreateAccountDto(String ownerName, String password) {
        this.ownerName = ownerName;
        this.password = password;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
