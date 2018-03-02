package com.jmwangi.bankaccount.services;

import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.model.ErrorModel;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.repositories.AccountRepository;
import com.jmwangi.bankaccount.utils.DateHelpers;
import com.jmwangi.bankaccount.utils.ErrorMessages;
import com.jmwangi.bankaccount.utils.Limits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

        // return if deposit amount is less than zero
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return new ErrorModel(ErrorMessages.DEPOSIT_LESS_THAN_ZERO);
        }

        // return if deposit amount exceeds maximum transaction amount
        if (amount.compareTo(Limits.MAX_DEPOSIT_PER_TRANSACTION) > 0) {
            return new ErrorModel(ErrorMessages.EXCEED_MAXIMUM_DEPOSIT_PER_TRANSACTION);
        }

        // check if maximum deposit frequency has been reached
        List<AccountTransaction> transactions =
                accountRepository.findByTimestampBetween(
                                DateHelpers.getStartOfDay(new Date()).getTime(),
                                DateHelpers.getEndOfDay(new Date()).getTime());

        long transactionsCount = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .count();

        if (transactionsCount == Limits.MAX_DEPOSIT_FREQUENCY) {
            return new ErrorModel(ErrorMessages.MAXIMUM_DEPOSIT_FREQUENCY_REACHED);
        }

        BigDecimal total = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(AccountTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(Limits.MAX_DEPOSIT_FOR_THE_DAY) == 0) {
            return new ErrorModel(ErrorMessages.MAX_DEPOSIT_FOR_DAY_REACHED);
        }

        if (total.add(amount).compareTo(Limits.MAX_DEPOSIT_FOR_THE_DAY) > 0) {
            BigDecimal x = Limits.MAX_DEPOSIT_FOR_THE_DAY.subtract(total);
            return new ErrorModel(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_DEPOSIT_FOR_DAY + x.toString());
        }


        AccountTransaction accountTransaction = accountRepository.findTopByOrderByTimestampDesc();

        // create new transaction
        AccountTransaction newTransaction = new AccountTransaction();
        newTransaction.setAccountNo(accountNumber);
        newTransaction.setAmount(amount);
        newTransaction.setTimestamp(new Date().getTime());
        newTransaction.setBalance(accountTransaction.getBalance().add(amount));

        // save transaction
        return accountRepository.save(newTransaction);

    }



    /*
    Get the last transaction, balance
    indicated is the current balance
    */
    public AccountTransaction balance() {
        return accountRepository.findTopByOrderByTimestampDesc();
    }

}
