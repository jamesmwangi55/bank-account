package com.jmwangi.bankaccount.repositories;

import com.jmwangi.bankaccount.model.AccountTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findTopByOrderByTimestampDesc_thenReturnAccountRepository() {
        // given
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());
        entityManager.persist(accountTransaction);
        entityManager.flush();

        // when found
        AccountTransaction found = accountRepository.findTopByOrderByTimestampDesc();

        // then
        assertThat(found.getAccountNo()).isEqualTo(accountTransaction.getAccountNo());
    }
}