package com.redonion.tcg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.redonion.tcg.model.User;
import com.redonion.tcg.repository.CardRepository;
import com.redonion.tcg.service.UserService;

@Controller
@RequestMapping("/shop")
public class ShopController {
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String showShop(Model model) {
        // Get current user if authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }

        // Add cards to the model
        model.addAttribute("cards", cardRepository.findAll());
        
        return "shop";
    }
}
