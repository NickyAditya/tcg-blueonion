package com.redonion.tcg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.redonion.tcg.model.UserInventory;

@Repository
public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
    @Modifying
    @Query("DELETE FROM UserInventory ui WHERE ui.user.id = ?1")
    void deleteByUserId(Integer userId);
}
