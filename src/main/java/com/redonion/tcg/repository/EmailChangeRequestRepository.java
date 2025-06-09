package com.redonion.tcg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.redonion.tcg.model.EmailChangeRequest;
import com.redonion.tcg.model.User;

@Repository
public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, Integer> {
    List<EmailChangeRequest> findByUserAndStatus(User user, EmailChangeRequest.RequestStatus status);
    List<EmailChangeRequest> findByStatus(EmailChangeRequest.RequestStatus status);
    
    @Modifying
    void deleteByUser(User user);
}
