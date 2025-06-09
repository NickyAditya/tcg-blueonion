package com.redonion.tcg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redonion.tcg.model.EmailChangeRequest;
import com.redonion.tcg.model.User;
import com.redonion.tcg.service.EmailChangeRequestService;
import com.redonion.tcg.service.UserService;

@RestController
@RequestMapping("/api/email-change")
public class EmailChangeRequestController {

    @Autowired
    private EmailChangeRequestService emailChangeRequestService;

    @Autowired
    private UserService userService;

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@RequestBody Map<String, String> request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        EmailChangeRequest changeRequest = emailChangeRequestService.createRequest(
            user, 
            request.get("newEmail")
        );
        
        return ResponseEntity.ok(changeRequest);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<EmailChangeRequest>> getPendingRequests() {
        List<EmailChangeRequest> requests = emailChangeRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmailChangeRequest>> getAllRequests() {
        List<EmailChangeRequest> requests = emailChangeRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = userService.findByUsername(auth.getName());
        
        emailChangeRequestService.processRequest(id, admin, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deny")
    public ResponseEntity<?> denyRequest(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = userService.findByUsername(auth.getName());
        
        emailChangeRequestService.processRequest(id, admin, false);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/status")
    public ResponseEntity<?> getUserRequestStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        EmailChangeRequest activeRequest = emailChangeRequestService.getActiveRequest(user);
        return ResponseEntity.ok(activeRequest);
    }
}
