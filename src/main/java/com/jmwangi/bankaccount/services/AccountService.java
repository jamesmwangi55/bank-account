package com.jmwangi.bankaccount.services;

import com.jmwangi.bankaccount.utils.ErrorMessages;
import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountTransaction create(AccountTransaction accountTransaction) {
        return accountRepository.save(accountTransaction);
    }

    public AccountTransaction debit() {
        return null;
    }

    public Object credit(TransactionHelper transactionHelper) {
        long accountNumber = transactionHelper.getAccountNumber();
        BigDecimal amount = transactionHelper.getAmount();

        // stop here if deposit amount is less than zero
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return new Error(ErrorMessages.DEPOSIT_LESS_THAN_ZERO);
        }

        // stop here if deposit amount exceeds maximum transaction amount
        if (amount.compareTo(new BigDecimal(40000)) < 0) {

        }

        if (amount.compareTo(new BigDecimal(40000)) < 0) {
            // get the last transaction for account
            AccountTransaction accountTransaction = accountRepository.findTopByOrderByTimestampDesc();

            // create new transaction
            AccountTransaction newTransaction = new AccountTransaction();
            accountTransaction.setAccountNo(accountNumber);
            accountTransaction.setAmount(amount);
            accountTransaction.setTimestamp(new Date());
            accountTransaction.setBalance(accountTransaction.getBalance().add(amount));

            // save transaction
            return accountRepository.save(newTransaction);

        } else {

        }

    }

    /*
    Get the last transaction, balance
    indicated is the current balance
    */
    public AccountTransaction balance() {
        return  accountRepository.findTopByOrderByTimestampDesc();
    }

}
