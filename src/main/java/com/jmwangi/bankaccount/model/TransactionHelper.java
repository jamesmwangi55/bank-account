package com.jmwangi.bankaccount.model;

import java.math.BigDecimal;

/*
* Pojo class to be used with
* deposit / withdraw.
* */
public class TransactionHelper {
    private BigDecimal amount;

    public TransactionHelper() {
    }

    public TransactionHelper(BigDecimal amount) {
        this.amount = amount;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
