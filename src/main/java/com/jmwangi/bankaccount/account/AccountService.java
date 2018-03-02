package com.jmwangi.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public AccountTransaction credit() {
        return null;
    }


    public AccountTransaction balance(Long id) {
        return  accountRepository.findOne(id);
    }

}
