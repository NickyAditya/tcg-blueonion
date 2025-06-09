package com.redonion.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        String originalUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);
            model.addAttribute("errorMessage", HttpStatus.valueOf(statusCode).getReasonPhrase());

            // Menentukan halaman berdasarkan URL original
            if (originalUri != null) {
                if (originalUri.contains("/admin")) {
                    return "error"; // Gunakan error.html untuk admin
                } else if (originalUri.contains("/user")) {
                    return "error"; // Gunakan error.html untuk user
                } else if (originalUri.contains("/shop")) {
                    return "error"; // Gunakan error.html untuk shop
                } else if (originalUri.contains("/mtg")) {
                    return "error"; // Gunakan error.html untuk MTG
                } else if (originalUri.contains("/pokemon")) {
                    return "error"; // Gunakan error.html untuk Pokemon
                } else if (originalUri.contains("/yugioh")) {
                    return "error"; // Gunakan error.html untuk Yu-Gi-Oh
                }
            }
        }

        // Default fallback ke halaman error umum
        return "error";
    }
}