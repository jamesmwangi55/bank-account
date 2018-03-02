package com.jmwangi.bankaccount.controllers;

import com.jmwangi.bankaccount.model.ErrorModel;
import com.jmwangi.bankaccount.model.TransactionHelper;
import com.jmwangi.bankaccount.services.AccountService;
import com.jmwangi.bankaccount.model.AccountTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<?> debit(@RequestBody TransactionHelper transactionHelper) {

        Object object = accountService.debit(transactionHelper);

        return getResponseEntity(object);
    }

    private ResponseEntity<?> getResponseEntity(Object object) {
        if (object instanceof ErrorModel) {
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> credit(@RequestBody TransactionHelper transactionHelper) {

        Object object = accountService.credit(transactionHelper);

        return getResponseEntity(object);
    }

    @GetMapping("/accounts/balance")
    public AccountTransaction balance() {
        return accountService.balance();
    }

}
