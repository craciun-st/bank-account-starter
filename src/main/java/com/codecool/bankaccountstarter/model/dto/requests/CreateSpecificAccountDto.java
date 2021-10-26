package com.codecool.bankaccountstarter.model.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.codecool.bankaccountstarter.model.Account.DEFAULT_ACCOUNT_CODE_PATTERN;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSpecificAccountDto extends CreateAccountDto {



    @NotBlank
    @Pattern(regexp = DEFAULT_ACCOUNT_CODE_PATTERN)
    protected String code;

    public CreateSpecificAccountDto(String ownerName, String password) {
        super(ownerName, password);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
