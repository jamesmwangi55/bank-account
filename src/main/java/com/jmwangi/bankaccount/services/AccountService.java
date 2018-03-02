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

    public Object debit(TransactionHelper transactionHelper) {

        long accountNumber = transactionHelper.getAccountNumber();
        BigDecimal amount = transactionHelper.getAmount();

        Object x = validateWithdrawTransaction(amount);
        if (x != null) {
            return x;
        }

        List<AccountTransaction> transactions = getAccountTransactions();

        if (isWithdrawalFrequencyReached(transactions)) {
            return new ErrorModel(ErrorMessages.MAX_WITHDRAWAL_FREQUENCY_FOR_DAY_REACHED);
        }

        BigDecimal total = getTotalWithdrawnForDay(transactions);


        if (total.compareTo(Limits.MAX_WITHDRAWAL_FOR_THE_DAY) == 0) {
            return new ErrorModel(ErrorMessages.MAX_WITHDRAWAL_FOR_DAY_REACHED);
        }

        // find last transaction
        AccountTransaction accountTransaction = accountRepository.findTopByOrderByTimestampDesc();

        Object withdrawable = validateMaxWithdrawalNotExceeded(amount, total, accountTransaction);
        if (withdrawable != null) {
            return withdrawable;
        }

        // return if withdrawal amount exceeds balance
        if (amount.compareTo(accountTransaction.getBalance()) > 0) {
            return new ErrorModel(ErrorMessages.WITHDRAWAL_AMOUNT_EXCEEDS_BALANCE + accountTransaction.getBalance());
        }

        AccountTransaction newTransaction = new AccountTransaction();
        newTransaction.setAccountNo(accountNumber);
        newTransaction.setAmount(amount.negate());
        newTransaction.setBalance(accountTransaction.getBalance().subtract(amount));
        newTransaction.setTimestamp(new Date().getTime());

        // save and return transaction
        return accountRepository.save(newTransaction);
    }

    private Object validateMaxWithdrawalNotExceeded(BigDecimal amount, BigDecimal total, AccountTransaction accountTransaction) {
        if (total.add(amount).compareTo(Limits.MAX_WITHDRAWAL_FOR_THE_DAY) > 0) {
            BigDecimal withdrawable = Limits.MAX_WITHDRAWAL_FOR_THE_DAY.subtract(total);

            // check if amount exceeds balance
            if (withdrawable.compareTo(accountTransaction.getBalance()) > 0) {
                return new ErrorModel(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_WITHDRAWAL_FOR_DAY + accountTransaction.getBalance());
            }

            return new ErrorModel(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_WITHDRAWAL_FOR_DAY + withdrawable.toString());
        }
        return null;
    }

    private BigDecimal getTotalWithdrawnForDay(List<AccountTransaction> transactions) {
        // return if total has reached max withdrawal amount for day
        return transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(accountTransaction -> accountTransaction.getAmount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isWithdrawalFrequencyReached(List<AccountTransaction> transactions) {
        // getWithdrawal transaction count
        long transactionsCount = transactions.stream()
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .count();

        // return if transaction count exceeds maximum daily withdrawal frequency
        if (transactionsCount == Limits.MAX_WITHDRAWAL_FREQUENCY) {
            return true;
        }
        return false;
    }

    private List<AccountTransaction> getAccountTransactions() {
        return accountRepository.findByTimestampBetween(
                DateHelpers.getStartOfDay(new Date()).getTime(),
                DateHelpers.getEndOfDay(new Date()).getTime()
        );
    }

    private Object validateWithdrawTransaction(BigDecimal amount) {
        // return if debit amount is greater than zero
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return new ErrorModel(ErrorMessages.WITHDRAWAL_LESS_THAN_ZERO);
        }

        // return if debit amount exceeds max withdrawal per transaction
        if (amount.compareTo(Limits.MAX_WITHDRAWAL_PER_TRANSACTION) > 0) {
            return new ErrorModel(ErrorMessages.EXCEED_MAXIMUM_WITHDRAWAL_PER_TRANSACTION);
        }
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

        List<AccountTransaction> transactions = getAccountTransactions();

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

        // return if max deposits is already reached
        if (total.compareTo(Limits.MAX_DEPOSIT_FOR_THE_DAY) == 0) {
            return new ErrorModel(ErrorMessages.MAX_DEPOSIT_FOR_DAY_REACHED);
        }

        // return if deposit will exceed max deposits
        if (total.add(amount).compareTo(Limits.MAX_DEPOSIT_FOR_THE_DAY) > 0) {
            BigDecimal x = Limits.MAX_DEPOSIT_FOR_THE_DAY.subtract(total);
            return new ErrorModel(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_DEPOSIT_FOR_DAY + x.toString());
        }


        // find last transaction
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
