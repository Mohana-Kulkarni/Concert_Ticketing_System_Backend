package com.example.concertsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/artists/*").permitAll()
                        .requestMatchers("/events/*").permitAll()
                        .requestMatchers("/img/*").permitAll()
                        .requestMatchers("/organisers/*").permitAll()
                        .requestMatchers("/places/*").permitAll()
                        .requestMatchers("/tickets/*").permitAll()
                        .requestMatchers("/tiers/*").permitAll()
                        .requestMatchers("/users/*").permitAll()
                        .requestMatchers("/venues/*").permitAll()
                );

                http.httpBasic(Customizer.withDefaults());
                http.cors(Customizer.withDefaults());
                http.csrf(csrf -> csrf.disable());

                return http.build();
    }

}
