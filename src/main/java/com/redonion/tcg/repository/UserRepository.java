package com.redonion.tcg.repository;

import com.redonion.tcg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNama(String nama);
}