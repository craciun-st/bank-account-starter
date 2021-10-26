package com.codecool.bankaccountstarter.service;

import com.codecool.bankaccountstarter.model.Currency;
import com.codecool.bankaccountstarter.model.NumericTransformations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class NumericService implements NumericTransformations {


    private ExchangeRateService exchangeRateService;

    @Autowired
    public NumericService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }



    @Override
    public double mapFixedToFloat(long value) {
        // can be made more precise
        return value / 100.00;
    }

    @Override
    public long mapFloatToFixed(double value) {
        // can be made more precise
        return Math.round(value * 100);
    }

    @Override
    public long mapToEuroCents(long valueInForeignCents, Currency foreignCurrency) {
        // can be made more precise
        double scalar = exchangeRateService.getScalarValueOfForeignUnit(foreignCurrency, Currency.EUR);
        return Math.round(valueInForeignCents * scalar);
    }



    @Override
    public long mapFromEuroCents(long valueInEuroCents, Currency toCurrency) {
        // can be made more precise
        double scalar = exchangeRateService.getScalarValueOfForeignUnit(Currency.EUR, toCurrency);
        return Math.round(valueInEuroCents * scalar);
    }

    public long mapFromAmountInForeignCurrency(long amountInForeign, Currency foreignCurrency, Currency properCurrency) {
        long amountDepositedInProperCurrency = 0L;
        if (properCurrency == foreignCurrency) {
            amountDepositedInProperCurrency = amountInForeign;
        } else {
            if (properCurrency == Currency.EUR) {
                amountDepositedInProperCurrency = mapToEuroCents(amountInForeign, foreignCurrency);
            } else {
                // TODO implement a more realistic conversion;
                // the 2 maps are each other's inverse currently, but with a real exchange rate, the maps would
                // not be so, due to buy/sell differences. Also, this is not very precise
                amountDepositedInProperCurrency =
                        mapFromEuroCents(
                                mapToEuroCents(amountInForeign, foreignCurrency),
                                properCurrency
                        );
            }
        }
        return amountDepositedInProperCurrency;
    }

}
