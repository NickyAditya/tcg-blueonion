package com.redonion.tcg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.redonion.tcg.model.User;
import com.redonion.tcg.service.UserService;

@Controller
public class WebController {

    @Autowired
    private UserService userService;    @GetMapping({"/", "/index"})
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getName().equals("anonymousUser")) {
            try {
                User user = userService.findByUsername(auth.getName());
                if (user != null) {
                    model.addAttribute("user", user);
                    model.addAttribute("username", auth.getName());
                    return "indexAdUs";
                }
            } catch (Exception e) {
                System.err.println("Error loading user data: " + e.getMessage());
            }
        }
        return "index";
    }

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("adminName", auth.getName());
        return "admin";
    }

    @GetMapping("/pokemon")
    public String pokemon() {
        return "pokemon";
    }

    @GetMapping("/yugioh")
    public String yugioh() {
        return "yugioh";
    }

    @GetMapping("/mtg")
    public String mtg() {
        return "mtg";
    }

    @GetMapping("/userInventory")
    public String userInventory() {
        return "userInventory";
    }

    @GetMapping("/booster")
    public String booster() {
        return "booster";
    }
}
