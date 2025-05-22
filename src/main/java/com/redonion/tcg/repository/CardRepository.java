package com.redonion.tcg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.redonion.tcg.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    // kamu bisa tambah method custom di sini kalau perlu
}
