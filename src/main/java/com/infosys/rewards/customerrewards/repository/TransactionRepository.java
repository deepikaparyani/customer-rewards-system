package com.infosys.rewards.customerrewards.repository;

import com.infosys.rewards.customerrewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerCustomerId(Long customerId);

    List<Transaction> findByCustomerCustomerIdAndTransactionDateBetween(
            Long customerId,
            LocalDate fromDate,
            LocalDate toDate
    );
}