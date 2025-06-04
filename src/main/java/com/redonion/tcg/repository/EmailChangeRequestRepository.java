package com.redonion.tcg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redonion.tcg.model.EmailChangeRequest;
import com.redonion.tcg.model.User;

public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, Integer> {
    List<EmailChangeRequest> findByUserAndStatus(User user, EmailChangeRequest.RequestStatus status);
    List<EmailChangeRequest> findByStatus(EmailChangeRequest.RequestStatus status);
}
