package com.jmwangi.bankaccount;

import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {

    private final AccountRepository accountRepository;

    @Autowired
    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());
        accountRepository.save(accountTransaction);
    }
}
