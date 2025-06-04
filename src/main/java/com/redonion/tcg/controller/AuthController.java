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
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {        try {
            // Validate input
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty() ||
                name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "All fields are required");
                return "redirect:/sign";
            }

            // Check if username already exists
            if (userRepository.findByNama(username).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/sign";
            }

            User user = new User();
            user.setNama(username);
            user.setEmail(name + "@example.com");
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(UserRole.USER);            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/sign";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/sign";
        }
    }
}