package com.example.bookrent.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final List<String> PUBLIC_APIS = Arrays.asList(
            "/api/books/popular",
            "/api/books/highly-rated",
            "/api/books/search"
    );

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // Allow frames for H2 Console
                )
                .sessionManagement(mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // stateless session for JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Whitelist H2 console
                        .requestMatchers(PUBLIC_APIS.toArray(new String[0])).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated() // Protect /api endpoints
                        .anyRequest().permitAll()) // Permit all other requests (optional)
                .addFilterBefore(new JwtTokenValidator(), UsernamePasswordAuthenticationFilter.class)  // Add JWT filter before UsernamePasswordAuthenticationFilter
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless application
                .cors(cors -> cors.configurationSource(getCorsConfigurationSource()))  // CORS configuration
                .httpBasic(Customizer.withDefaults())  // Disable HTTP Basic authentication
                .formLogin(Customizer.withDefaults())  // Disable Form login
                .logout(logout -> logout.logoutUrl("/logout").permitAll());  // Optional: configure logout behavior

        return http.build();
    }

    private CorsConfigurationSource getCorsConfigurationSource() {
        return request -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(Arrays.asList("*"));
            corsConfig.setAllowCredentials(true);
            corsConfig.setAllowedHeaders(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            corsConfig.setExposedHeaders(Arrays.asList("Authorization"));
            corsConfig.setMaxAge(3600L);
            return corsConfig;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
