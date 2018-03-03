package com.jmwangi.bankaccount.services;

import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.model.TransactionHelper;
import org.springframework.stereotype.Component;

public interface AccountService {
    Object debit(TransactionHelper transactionHelper);
    Object credit(TransactionHelper transactionHelper);
    AccountTransaction balance();
}
