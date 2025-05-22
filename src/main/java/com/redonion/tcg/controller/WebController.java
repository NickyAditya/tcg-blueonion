package com.redonion.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }

    @GetMapping("/admin")
    public String admin() {
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
}
