package com.jmwangi.bankaccount.account;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Transactions {
    @Id
    private Long id;
    private Long amount;
    private Long balance;
    private Long timestamp;


}
