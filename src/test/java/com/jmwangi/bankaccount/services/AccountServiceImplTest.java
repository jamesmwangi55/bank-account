package com.jmwangi.bankaccount.services;

import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.repositories.AccountRepository;
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
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {
        @Bean
        public AccountService accountService () {
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
    }

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void debit() {
    }

    @Test
    public void credit() {
    }

    @Test
    public void balance() {
        AccountTransaction found = accountRepository.findTopByOrderByTimestampDesc();

        assertThat(found).isNotEqualTo(null);
        assertThat(found.getBalance()).isEqualTo(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
    }
}