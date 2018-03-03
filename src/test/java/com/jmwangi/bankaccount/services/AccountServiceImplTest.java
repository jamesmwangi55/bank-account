package com.jmwangi.bankaccount.services;

import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.model.ErrorModel;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.repositories.AccountRepository;
import com.jmwangi.bankaccount.utils.DateHelpers;
import com.jmwangi.bankaccount.utils.ErrorMessages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {
        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl();
        }
    }

    @Autowired
    private AccountService accountService;

    @Before
    public void setup() {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());

        Mockito.when(accountRepository.findTopByOrderByTimestampDesc())
                .thenReturn(accountTransaction);

        Mockito.when(accountRepository.save(accountTransaction))
                .thenReturn(accountTransaction);
    }

    @MockBean
    private AccountRepository accountRepository;

//    @Test
//    public void debit() {
//
//    }

    @Test
    public void creditMoreThan40KShouldReturnErrorModel() {

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(50000));

        Object object = accountService.credit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage())
                .isEqualTo(ErrorMessages.EXCEED_MAXIMUM_DEPOSIT_PER_TRANSACTION);
    }

    @Test
    public void creditLessThanZeroShouldReturnErrorModel() {
        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(50000).negate());

        Object object = accountService.credit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage())
                .isEqualTo(ErrorMessages.DEPOSIT_LESS_THAN_ZERO);
    }

    @Test
    public void creditMoreThan4TimesShouldReturnErrorModel() {

        List<AccountTransaction> transactions = new ArrayList<>();

        IntStream.range(0, 4).forEach(i -> {
            AccountTransaction accountTransaction = new AccountTransaction();
            accountTransaction.setBalance(new BigDecimal(10000).setScale(2, RoundingMode.DOWN));
            accountTransaction.setAmount(new BigDecimal(10000).setScale(2, RoundingMode.DOWN));
            accountTransaction.setAccountNo(2929134324L);
            accountTransaction.setTimestamp(new Date().getTime());
            transactions.add(accountTransaction);
        });

        Mockito.when(accountRepository.findByTimestampBetween(DateHelpers.getStartOfDay(new Date()).getTime(),
                DateHelpers.getEndOfDay(new Date()).getTime()))
                .thenReturn(transactions);


        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(1000));

        Object object = accountService.credit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage()).isEqualTo(ErrorMessages.MAXIMUM_DEPOSIT_FREQUENCY_REACHED);
    }

    @Test
    public void creditMoreThan150KPerDayShouldReturnErrorAmountThatCanBeDeposited(){
        List<AccountTransaction> transactions = new ArrayList<>();

        IntStream.range(0, 3).forEach(i -> {
            AccountTransaction accountTransaction = new AccountTransaction();
            accountTransaction.setAmount(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
            accountTransaction.setBalance(new BigDecimal(40000).multiply(new BigDecimal(2)).setScale(2, RoundingMode.DOWN));
            accountTransaction.setAccountNo(2929134324L);
            accountTransaction.setTimestamp(new Date().getTime());
            transactions.add(accountTransaction);
        });

        Mockito.when(accountRepository.findByTimestampBetween(DateHelpers.getStartOfDay(new Date()).getTime(),
                DateHelpers.getEndOfDay(new Date()).getTime()))
                .thenReturn(transactions);

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(40000));

        Object object = accountService.credit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage()).isEqualTo(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_DEPOSIT_FOR_DAY + new BigDecimal(30000).setScale(2, RoundingMode.DOWN));
    }


    public void widhrawMoreThan20KShouldReturnErrorModel() {

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(50000));

        Object object = accountService.debit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage())
                .isEqualTo(ErrorMessages.EXCEED_MAXIMUM_WITHDRAWAL_PER_TRANSACTION);
    }

    @Test
    public void withdrawLessThanZeroShouldReturnErrorModel() {
        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(50000).negate());

        Object object = accountService.debit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage())
                .isEqualTo(ErrorMessages.WITHDRAWAL_LESS_THAN_ZERO);
    }

    @Test
    public void withdrawMoreThan3TimesShouldReturnErrorModel() {

        List<AccountTransaction> transactions = new ArrayList<>();

        IntStream.range(0, 3).forEach(i -> {
            AccountTransaction accountTransaction = new AccountTransaction();
            accountTransaction.setBalance(new BigDecimal(1000).setScale(2, RoundingMode.DOWN));
            accountTransaction.setAmount(new BigDecimal(1000).setScale(2, RoundingMode.DOWN).negate());
            accountTransaction.setAccountNo(2929134324L);
            accountTransaction.setTimestamp(new Date().getTime());
            transactions.add(accountTransaction);
        });

        Mockito.when(accountRepository.findByTimestampBetween(DateHelpers.getStartOfDay(new Date()).getTime(),
                DateHelpers.getEndOfDay(new Date()).getTime()))
                .thenReturn(transactions);


        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(1000));

        Object object = accountService.debit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage()).isEqualTo(ErrorMessages.MAX_WITHDRAWAL_FREQUENCY_FOR_DAY_REACHED);
    }

    @Test
    public void withdrawMoreThan50KPerDayShouldReturnErrorAmountThatCanBeWithdrawn(){
        List<AccountTransaction> transactions = new ArrayList<>();

        IntStream.range(0, 2).forEach(i -> {
            AccountTransaction accountTransaction = new AccountTransaction();
            accountTransaction.setAmount(new BigDecimal(20000).setScale(2, RoundingMode.DOWN).negate());
            accountTransaction.setBalance(new BigDecimal(20000).setScale(2, RoundingMode.DOWN));
            accountTransaction.setAccountNo(2929134324L);
            accountTransaction.setTimestamp(new Date().getTime());
            transactions.add(accountTransaction);
        });

        Mockito.when(accountRepository.findByTimestampBetween(DateHelpers.getStartOfDay(new Date()).getTime(),
                DateHelpers.getEndOfDay(new Date()).getTime()))
                .thenReturn(transactions);

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(20000));

        Object object = accountService.debit(transactionHelper);

        assertThat(object).isExactlyInstanceOf(ErrorModel.class);
        ErrorModel errorModel = (ErrorModel) object;
        assertThat(errorModel.getMessage())
                .isEqualTo(ErrorMessages.AMOUNT_WILL_EXCEED_MAX_WITHDRAWAL_FOR_DAY + new BigDecimal(10000).setScale(2, RoundingMode.DOWN));
    }



    @Test
    public void balance() {
        AccountTransaction found = accountService.balance();

        assertThat(found).isNotEqualTo(null);
        assertThat(found.getBalance()).isEqualTo(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
    }
}