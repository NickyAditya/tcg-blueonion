package com.redonion.tcg.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redonion.tcg.model.User;
import com.redonion.tcg.repository.EmailChangeRequestRepository;
import com.redonion.tcg.repository.UserInventoryRepository;
import com.redonion.tcg.repository.UserRepository;

@Service
public class UserService {    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserInventoryRepository userInventoryRepository;

    @Autowired
    private EmailChangeRequestRepository emailChangeRequestRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User save(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User user) {
        User existingUser = findById(user.getId_user());

        existingUser.setNama(user.getNama());
        existingUser.setEmail(user.getEmail());

        // Only update password if one is provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setRole(user.getRole());

        return userRepository.save(existingUser);
    }    @Transactional
    public void delete(Integer id) {
        User user = findById(id);
        if (user != null) {
            // Delete any email change requests
            emailChangeRequestRepository.deleteByUser(user);
            
            // Delete user's inventory records
            userInventoryRepository.deleteByIdUser(id);
            
            // Delete user's avatars if they exist
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                try {
                    Path avatarPath = Paths.get("src/main/resources/static" + user.getAvatar());
                    Files.deleteIfExists(avatarPath);
                } catch (IOException e) {
                    // Log error but continue with deletion
                    System.err.println("Failed to delete avatar file: " + e.getMessage());
                }
            }
            
            // Finally delete the user
            userRepository.deleteById(id);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByNama(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean updatePassword(User user, String oldPassword, String newPassword) {
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // Update to new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
