package com.jmwangi.bankaccount.controllers;

import com.jmwangi.bankaccount.model.ErrorModel;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.services.AccountServiceImpl;
import com.jmwangi.bankaccount.model.AccountTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountServiceImpl accountServiceImpl;

    @Autowired
    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    private ResponseEntity<?> getResponseEntity(Object object) {
        if (object instanceof ErrorModel) {
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<?> debit(@RequestBody TransactionHelper transactionHelper) {

        Object object = accountServiceImpl.debit(transactionHelper);

        return getResponseEntity(object);
    }


    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> credit(@RequestBody TransactionHelper transactionHelper) {

        Object object = accountServiceImpl.credit(transactionHelper);

        return getResponseEntity(object);
    }

    @GetMapping("/accounts/balance")
    public AccountTransaction balance() {
        return accountServiceImpl.balance();
    }

}
