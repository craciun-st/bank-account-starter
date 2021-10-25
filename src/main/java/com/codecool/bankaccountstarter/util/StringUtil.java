package com.codecool.bankaccountstarter.util;

import java.util.Random;

public class StringUtil {

    public static String generateRandomUpcaseAlphaNumericString(byte length, Long seed) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = length;

        // use seed when you need reproducible pseudo-random sequences
        Random random = ( seed != null ? new Random(seed) : new Random() );

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(  i -> (i <= 57 || i >= 65)  )  // filter non-alphanumeric ASCII codes
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
