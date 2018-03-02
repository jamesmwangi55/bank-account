package com.jmwangi.bankaccount.model;

import java.math.BigDecimal;

/*
* Pojo class to be used with
* deposit / withdraw.
* */
public class TransactionHelper {
    private Long accountNumber;
    private BigDecimal amount;

    public TransactionHelper() {
    }

    public TransactionHelper(Long accountNumber, BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }


    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
