package com.redonion.tcg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.redonion.tcg.model.User;
import com.redonion.tcg.model.User.UserRole;
import com.redonion.tcg.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    email == null || email.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "All fields are required");
                return "redirect:/sign";
            }

            // Validate email format
            if (!email.contains("@")) {
                redirectAttributes.addFlashAttribute("error", "Please enter a valid email address");
                return "redirect:/sign";
            }

            // Validate password complexity
            if (password.length() < 8 ||
                    !password.matches(".*[A-Z].*") ||
                    !password.matches(".*[a-z].*")) {
                redirectAttributes.addFlashAttribute("error",
                        "Password must be at least 8 characters long and contain uppercase and lowercase letters");
                return "redirect:/sign";
            }

            // Check if username already exists
            if (userRepository.findByNama(username).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/sign";
            }

            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Email is already registered");
                return "redirect:/sign";
            }

            User user = new User();
            user.setNama(username); // Set username for login
            user.setEmail(email); // Set the provided email
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(UserRole.USER);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/sign";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/sign";
        }
    }
}