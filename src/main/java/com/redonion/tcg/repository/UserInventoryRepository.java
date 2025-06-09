package com.redonion.tcg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.redonion.tcg.model.UserInventory;

@Repository
public interface UserInventoryRepository extends JpaRepository<UserInventory, Long> {
    Optional<UserInventory> findByIdUserAndIdKartu(Integer idUser, Long idKartu);
    List<UserInventory> findByIdUser(Integer idUser);
    
    @Modifying
    @Transactional
    void deleteByIdUser(Integer idUser);
}
