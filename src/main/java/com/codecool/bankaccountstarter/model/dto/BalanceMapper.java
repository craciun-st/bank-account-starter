package com.codecool.bankaccountstarter.model.dto;

import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.service.NumericService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {NumericService.class})
public abstract class BalanceMapper {

    protected NumericService numericService;


    @Mappings({
            @Mapping(target = "currency", source = "currency")
    })
    public abstract BalanceDto balanceDtoFromAccountEntity(Account accountEntity);

    @Autowired
    public void setNumericService(NumericService numericService) {
        this.numericService = numericService;
    }

    @AfterMapping
    public void updateBalance(@MappingTarget BalanceDto balanceDto, Account accountEntity) {
        balanceDto.setBalance(numericService.mapFixedToFloat(accountEntity.getBalanceInCents()));
    }
}
