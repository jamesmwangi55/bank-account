package com.jmwangi.bankaccount.controllers;

import com.jmwangi.bankaccount.model.Error;
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
    public ResponseEntity<?> debit(@RequestBody AccountTransaction accountTransaction) {

        return new ResponseEntity<Object>(accountTransaction, HttpStatus.OK);
    }


    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> credit(@RequestBody TransactionHelper transactionHelper) {

        Object object = accountService.credit(transactionHelper);

        if (object instanceof Error) {
          return  new ResponseEntity<Object>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Object>((AccountTransaction) object, HttpStatus.OK);
    }

    @GetMapping("/accounts/balance")
    public AccountTransaction balance() {
        return accountService.balance();
    }

    @PostMapping("accounts")
    public ResponseEntity<?> create(@RequestBody AccountTransaction accountTransaction) {
        AccountTransaction accountTransaction1 = accountService.create(accountTransaction);
        return new ResponseEntity<Object>(accountTransaction1, HttpStatus.OK);
    }

}
