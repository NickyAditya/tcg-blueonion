package com.redonion.tcg.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redonion.tcg.model.User;
import com.redonion.tcg.service.UserService;

@RestController
@RequestMapping("/api/settings")
public class UserSettingsController {

    @Autowired
    private UserService userService;

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateSettings(
            @RequestParam(required = false) String about,
            @RequestParam(required = false) String favoriteTags,
            @RequestParam(required = false) MultipartFile avatar) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());

        if (about != null) {
            user.setAbout(about);
        }
        
        if (favoriteTags != null) {
            user.setFavorite_tags(favoriteTags);
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = user.getId_user() + "_" + avatar.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(avatar.getInputStream(), uploadPath.resolve(fileName));
                user.setAvatar("/uploads/" + fileName);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        user = userService.update(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (userService.updatePassword(user, oldPassword, newPassword)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Invalid old password");
        }
    }

    @PostMapping("/remove-avatar")
    public ResponseEntity<?> removeAvatar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        user.setAvatar(null);
        userService.update(user);
        
        return ResponseEntity.ok().build();
    }
}
