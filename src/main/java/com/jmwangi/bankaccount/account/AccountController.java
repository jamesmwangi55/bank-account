package com.jmwangi.bankaccount.account;

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
    public AccountTransaction credit() {
        return accountService.credit();
    }

    @GetMapping("/accounts/balance")
    public AccountTransaction balance(@PathVariable Long id) {
        return accountService.balance(id);
    }

    @PostMapping("accounts")
    public ResponseEntity<?> create(@RequestBody AccountTransaction accountTransaction) {
        AccountTransaction accountTransaction1 = accountService.create(accountTransaction);
        return new ResponseEntity<Object>(accountTransaction1, HttpStatus.OK);
    }

}
