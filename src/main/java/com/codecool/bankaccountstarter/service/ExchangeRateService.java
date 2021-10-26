package com.codecool.bankaccountstarter.service;

import com.codecool.bankaccountstarter.model.Currency;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    public ExchangeRateService() {
    }

    public double getScalarValueOfForeignUnit(Currency fromForeign, Currency toLocal) {
        // can be expanded to get data from an API, a DB etc.

        if (fromForeign == toLocal) { return 1.00000000; }
        if (fromForeign != Currency.RON && toLocal != Currency.RON) {
            return 1.00000000;
        }
        if (fromForeign == Currency.RON) {
            return 1/4.500000;
        }
        if (toLocal == Currency.RON) {
            return 4.500000;
        } else {
            return 1.00000000;
        }
    }

}
