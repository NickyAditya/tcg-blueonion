package com.redonion.tcg.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("DEBUG: SecurityFilterChain initialized");
        
        http.csrf(csrf -> 
                csrf.ignoringRequestMatchers("/api/**")
            )
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers(
                        "/static/**", "/css/**", "/*.css", "/*.js", "/*.png",
                        "/*.jpg", "/*.jpeg", "/*.webp", "/textures/**",
                        "/texture/**", "/images/**", "/logo*", "/*.ico",
                        "/webjars/**", "/fonts/**", "/uploads/**","/error")
                    .permitAll()
                    .requestMatchers(
                            "/", "/index", "/sign", "/login", "/register", "/error",
                            "/pokemon", "/yugioh", "/mtg", "/register", "/logout")
                        .permitAll()
                    .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/settings/**").authenticated()
                    .requestMatchers("/user/**", "/userInventory", "/booster", "/settings", "/userSettings").authenticated()
                    .anyRequest().authenticated()
            )
            .formLogin(form -> 
                form.loginPage("/sign")
                    .loginProcessingUrl("/login")
                    .successHandler(new CustomLoginSuccessHandler())
                    .failureUrl("/sign?error=true")
                    .permitAll()
            )
            .logout(logout -> 
                logout.logoutUrl("/logout")
                    .logoutSuccessUrl("/sign?logout=true")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )
            .exceptionHandling(ex ->
                ex.accessDeniedPage("/error")
            );

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
                System.out.println("DEBUG: Redirecting to /user");
                response.sendRedirect("/user");
            }
        }
    }
}