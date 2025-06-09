package com.redonion.tcg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redonion.tcg.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Add custom query methods if needed
}
