package com.redonion.tcg.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redonion.tcg.model.EmailChangeRequest;
import com.redonion.tcg.model.User;
import com.redonion.tcg.repository.EmailChangeRequestRepository;
import com.redonion.tcg.repository.UserRepository;

@Service
public class EmailChangeRequestService {

    @Autowired
    private EmailChangeRequestRepository emailChangeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public EmailChangeRequest createRequest(User user, String newEmail) {
        // Check if there's already a pending request
        List<EmailChangeRequest> pendingRequests = emailChangeRequestRepository
            .findByUserAndStatus(user, EmailChangeRequest.RequestStatus.PENDING);
        
        if (!pendingRequests.isEmpty()) {
            throw new RuntimeException("You already have a pending email change request");
        }

        EmailChangeRequest request = new EmailChangeRequest();
        request.setUser(user);
        request.setCurrentEmail(user.getEmail());
        request.setRequestedEmail(newEmail);
        request.setRequestDate(new Date());
        request.setStatus(EmailChangeRequest.RequestStatus.PENDING);

        return emailChangeRequestRepository.save(request);
    }

    public List<EmailChangeRequest> getPendingRequests() {
        return emailChangeRequestRepository.findByStatus(EmailChangeRequest.RequestStatus.PENDING);
    }

    @Transactional
    public void processRequest(Integer requestId, User admin, boolean approved) {
        EmailChangeRequest request = emailChangeRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStatus() != EmailChangeRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Request has already been processed");
        }

        request.setProcessedBy(admin);
        request.setProcessedDate(new Date());
        request.setStatus(approved ? 
            EmailChangeRequest.RequestStatus.APPROVED : 
            EmailChangeRequest.RequestStatus.DENIED);

        if (approved) {
            User user = request.getUser();
            user.setEmail(request.getRequestedEmail());
            userRepository.save(user);
        }

        emailChangeRequestRepository.save(request);
    }

    public EmailChangeRequest getActiveRequest(User user) {
        List<EmailChangeRequest> pendingRequests = emailChangeRequestRepository
            .findByUserAndStatus(user, EmailChangeRequest.RequestStatus.PENDING);
        
        return pendingRequests.isEmpty() ? null : pendingRequests.get(0);
    }
}
