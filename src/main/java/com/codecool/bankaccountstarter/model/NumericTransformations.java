package com.codecool.bankaccountstarter.model;

public interface NumericTransformations {

    double mapFixedToFloat(long value);

    long mapFloatToFixed(double value);

    long mapToEuroCents(long valueInForeignCents, Currency foreignCurrency);

    long mapFromEuroCents(long valueInEuroCents, Currency toCurrency);

}
