package com.jmwangi.bankaccount.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jmwangi.bankaccount.model.AccountTransaction;
import com.jmwangi.bankaccount.model.ErrorModel;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.services.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void givenAccountTransaction_whenGetBalance_thenReturnJson() throws Exception {
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(40000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());

        given(accountService.balance()).willReturn(accountTransaction);

        mvc.perform(get("/accounts/balance")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNo", is(accountTransaction.getAccountNo())));
    }

    @Test
    public void givenAccountTransaction_whenDeposit_thenReturnAccountTransaction () throws Exception {

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(1000));

        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(1000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(10000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());

        given(accountService.credit(transactionHelper)).willReturn(accountTransaction);

        String requestJson = getRequestJson(transactionHelper);

        mvc.perform(post("/accounts/deposit")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) ;
    }

    @Test
    public void givenAccountTransaction_whenWithdraw_thenReturnAccountTransaction () throws Exception {

        TransactionHelper transactionHelper = new TransactionHelper();
        transactionHelper.setAmount(new BigDecimal(100));

        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setBalance(new BigDecimal(1000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAmount(new BigDecimal(10000).setScale(2, RoundingMode.DOWN));
        accountTransaction.setAccountNo(2929134324L);
        accountTransaction.setTimestamp(new Date().getTime());

        given(accountService.debit(transactionHelper)).willReturn(accountTransaction);

        String requestJson = getRequestJson(transactionHelper);

        mvc.perform(post("/accounts/deposit")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String getRequestJson(TransactionHelper transactionHelper) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(transactionHelper );
    }


}