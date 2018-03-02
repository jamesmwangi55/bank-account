package com.jmwangi.bankaccount.account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountTransaction, Long>{
   // public List<AccountTransaction>
}
