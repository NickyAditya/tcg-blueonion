package com.redonion.tcg.service;

import com.redonion.tcg.model.User;
import com.redonion.tcg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNama(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("DEBUG: Login attempt for username: " + username);
        System.out.println("DEBUG: Found user: " + user.getNama() + ", role: " + user.getRole());

        // Convert enum to Spring Security role format
        String roleWithPrefix = "ROLE_" + user.getRole().name();

        return new org.springframework.security.core.userdetails.User(
                user.getNama(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix)));
    }
}