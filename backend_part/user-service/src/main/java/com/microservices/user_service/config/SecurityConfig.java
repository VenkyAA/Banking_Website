package com.microservices.user_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // authentication
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/users/register", "/users/login", "/users/username/**").permitAll()
                        .requestMatchers("/accounts", "/accounts/**").hasRole("ADMIN")
                        .requestMatchers("/accounts/{id}", "/accounts/{id}/deposit", "/accounts/{id}/withdraw", "/accounts/{id}/balance").hasRole("USER")
                        .requestMatchers("/transactions", "/transactions/**", "/transactions/account/**").hasRole("ADMIN")
                        .requestMatchers("/transactions", "/transactions/transfer", "/transactions/withdraw", "/transactions/deposit", "/transactions/account/{id}", "/transactions/account/{id}/balance").hasRole("USER")
                        .requestMatchers("/profiles", "/profiles/**").hasRole("ADMIN")
                        .requestMatchers("/profile", "/profiles/{id}", "/profiles/{id}").hasRole("USER")
                        .requestMatchers("/loans", "/loans/**", "/loans/account/**").hasRole("ADMIN")
                        .requestMatchers("/loans", "/loans/{id}", "/loans/repay/{id}").hasRole("USER")
                        .anyRequest().authenticated())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
