
package com.securebank.repository;

import com.securebank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccountOrderByDateDesc(String fromAccount, String toAccount);
    List<Transaction> findAllByOrderByDateDesc();
}
