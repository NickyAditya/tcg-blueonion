package com.redonion.tcg.config;

import com.redonion.tcg.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("DEBUG: SecurityFilterChain initialized");
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/static/**", "/css/**", "/*.css", "/*.js", "/*.png",
                                "/*.jpg", "/*.jpeg", "/*.webp", "/textures/**",
                                "/texture/**", "/images/**", "/logo*", "/*.ico",
                                "/webjars/**", "/fonts/**")
                        .permitAll()
                        .requestMatchers(
                                "/", "/index", "/sign", "/login", "/register", "/error",
                                "/pokemon", "/yugioh", "/mtg")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/sign")
                        .loginProcessingUrl("/login")
                        .successHandler(new CustomLoginSuccessHandler())
                        .failureUrl("/sign?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error"));

        return http.build();
    }

    public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            System.out.println("DEBUG: Login success for user: " + authentication.getName());
            System.out.println("DEBUG: Roles: " + roles);
            if (roles.contains("ROLE_ADMIN")) {
                System.out.println("DEBUG: Redirecting to /admin");
                response.sendRedirect("/admin");
            } else {
                System.out.println("DEBUG: Redirecting to /");
                response.sendRedirect("/");
            }
        }
    }
}