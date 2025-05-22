package com.redonion.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError() {
        // Bisa diubah ke halaman error custom jika diperlukan
        return "sign";
    }
}
