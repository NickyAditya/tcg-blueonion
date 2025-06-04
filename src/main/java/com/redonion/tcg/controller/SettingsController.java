package com.redonion.tcg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.redonion.tcg.model.User;
import com.redonion.tcg.service.UserService;

@Controller
public class SettingsController {

    @Autowired
    private UserService userService;    @GetMapping({"/settings", "/userSettings"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String showSettings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        model.addAttribute("username", auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("emailChangeRequest", false);

        return "userSettings";
    }
}
