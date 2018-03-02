package com.jmwangi.bankaccount.utils;

import java.math.BigDecimal;

/*
* simple java class with deposit and withdrawal limits
* */
public class Limits {
    // deposit
    public static final BigDecimal MAX_DEPOSIT_FOR_THE_DAY = new BigDecimal(150000);
    public static final BigDecimal MAX_DEPOSIT_PER_TRANSACTION = new BigDecimal(40000);

    // withdrawal
    public static final BigDecimal MAX_WITHDRAWAL_FOR_THE_DAY = new BigDecimal(50000);
    public static final BigDecimal MAX_WITHDRAWAL_PER_TRANSACTION = new BigDecimal(20000);

    // frequency
    public static final int MAX_DEPOSIT_FREQUENCY = 4;
    public static final int MAX_WITHDRAWAL_FREQUENCY = 3;

}
