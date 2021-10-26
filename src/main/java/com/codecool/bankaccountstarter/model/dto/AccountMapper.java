package com.codecool.bankaccountstarter.model.dto;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.service.NumericService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", uses = {NumericService.class})
public abstract class AccountMapper {

    protected NumericService numericService;


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "ownerInfo.id", source = "ownerInfo.id"),
            @Mapping(target = "ownerInfo.name", source = "ownerInfo.name")
    })
    public abstract AccountDto accountEntityToDto(Account accountEntity);

    @Autowired
    public void setNumericService(NumericService numericService) {
        this.numericService = numericService;
    }

    @AfterMapping
    public void updateBalance(@MappingTarget AccountDto accountDto, Account accountEntity) {
        accountDto.setBalance(numericService.mapFixedToFloat(accountEntity.getBalanceInCents()));
    }







}
