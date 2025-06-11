package com.redonion.tcg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.redonion.tcg.model.User;
import com.redonion.tcg.model.UserInventory;
import com.redonion.tcg.service.UserInventoryService;
import com.redonion.tcg.service.UserService;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInventoryService userInventoryService;

    @GetMapping("/user")
    public String showUserProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        model.addAttribute("user", user);
        
        // Calculate time since join
        long diff = new java.util.Date().getTime() - user.getCreatedAt().getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        String joinedTime;
        
        if (days > 365) {
            joinedTime = (days / 365) + " years ago";
        } else if (days > 30) {
            joinedTime = (days / 30) + " months ago";
        } else if (days > 7) {
            joinedTime = (days / 7) + " weeks ago";
        } else if (days > 0) {
            joinedTime = days + " days ago";
        } else {
            joinedTime = "today";
        }
        
        model.addAttribute("joinedTime", joinedTime);

        // Get user's inventory
        List<UserInventory> userInventory = userInventoryService.getUserInventory(user.getId_user());
        model.addAttribute("userInventory", userInventory);
        
        return "user";
    }
}
