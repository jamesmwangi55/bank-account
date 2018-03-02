package com.jmwangi.bankaccount.repositories;

import com.jmwangi.bankaccount.model.AccountTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface AccountRepository extends CrudRepository<AccountTransaction, Long>{
   AccountTransaction findTopByOrderByTimestampDesc();
   List<AccountTransaction> findByTimestamp_Date(Date date);
}
