package com.co.neoristest.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authHttp) -> authHttp
                        .requestMatchers("/api/v1/usuarios/authorized").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/{userId}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/detail/{userId}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/external/{username}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/{userId}").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/{userId}").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/microservices-users"))
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(withDefaults()));

        return http.build();
    }

}
