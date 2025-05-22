package com.redonion.tcg.controller;

import com.redonion.tcg.model.User;
import com.redonion.tcg.model.User.UserRole;
import com.redonion.tcg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            RedirectAttributes redirectAttributes) {

        // Check if username already exists
        if (userRepository.findByNama(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/sign";
        }

        User user = new User();
        user.setNama(username);
        user.setEmail(name + "@example.com"); // You might want to add email field to the form later
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.USER); // Setting default role using enum

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");

        return "redirect:/sign";
    }
}