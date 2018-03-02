package com.jmwangi.bankaccount.repositories;

import com.jmwangi.bankaccount.model.AccountTransaction;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountTransaction, Long>{
   AccountTransaction findFirstByTimestampOrderByTimestampDesc(int number);
   AccountTransaction findTopByOrderByTimestampDesc();
}
