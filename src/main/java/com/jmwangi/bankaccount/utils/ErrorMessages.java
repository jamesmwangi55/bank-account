package com.jmwangi.bankaccount.utils;

public class ErrorMessages {
    public static final String DEPOSIT_LESS_THAN_ZERO = "Deposit amount is less than zero";
    public static final String EXCEED_MAXIMUM_DEPOSIT_PER_TRANSACTION = "Exceeded maximum deposit per transaction";
    public static final String MAXIMUM_DEPOSIT_FREQUENCY_REACHED = "Maximum deposit transactions per day reached";
    public static final String MAX_DEPOSIT_FOR_DAY_REACHED = "Maximum deposit for the day reached";
    public static final String AMOUNT_WILL_EXCEED_MAX_DEPOSIT_FOR_DAY = "Deposit will exceed maximum deposit for the day. You can only deposit $";
    public static final String WITHDRAWAL_SHOULD_BE_NEGATIVE = "Withdrawal amount should be negative";
    public static final String EXCEED_MAXIMUM_WITHDRAWAL_PER_TRANSACTION = "Exceeded maximum withdrawal per transaction";
    public static final String MAX_WITHDRAWAL_FOR_DAY_REACHED = "Maximum withdrawal for the day reached";
    public static final String MAX_WITHDRAWAL_FREQUENCY_FOR_DAY_REACHED = "Maximum withdrawal frequency for day reached";
    public static final String WITHDRAWAL_LESS_THAN_ZERO = "Withdrawal amount is less than zero";
    public static final String AMOUNT_WILL_EXCEED_MAX_WITHDRAWAL_FOR_DAY = "Withdrawal will exceed maximum withdrawal for the day. You can only withdrawal $";
    public static final String WITHDRAWAL_AMOUNT_EXCEEDS_BALANCE = "Withdrawal amount exceeds balance";
}
