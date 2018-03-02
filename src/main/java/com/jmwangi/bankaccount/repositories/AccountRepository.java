package com.jmwangi.bankaccount.repositories;

import com.jmwangi.bankaccount.model.AccountTransaction;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface AccountRepository extends CrudRepository<AccountTransaction, Long>{
   AccountTransaction findTopByOrderByTimestampDesc();
   List<AccountTransaction> findByTimestampDate(@Temporal(TemporalType.DATE) Date date);
}
