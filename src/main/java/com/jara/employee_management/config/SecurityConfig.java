package com.jara.employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jara.employee_management.repository.AppUserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// Para diferenciar permisos de user y admin
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                // para que funcione con session
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                // hasta aquí

                                .authorizeHttpRequests(auth -> auth

                                                // ==========================================
                                                // ENDPOINTS PÚBLICOS
                                                // ==========================================

                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/v3/api-docs/**")
                                                .permitAll()

                                                // ==========================================
                                                // ENDPOINTS PROTEGIDOS
                                                // ==========================================
                                                /*
                                                 * .requestMatchers("/api/employee/**")
                                                 * .authenticated()
                                                 */
                                                .requestMatchers(HttpMethod.GET, "/api/employee/**")
                                                .hasAnyRole("ADMIN", "HR", "USER")

                                                .requestMatchers("/api/employee/**")
                                                .hasRole("ADMIN")

                                                // ==========================================
                                                // CUALQUIER OTRA PETICIÓN
                                                // ==========================================

                                                .anyRequest()
                                                .authenticated())

                                /*
                                 * Para las peticiones protegidas, espera un Bearer Token JWT y utiliza
                                 * JwtDecoder para validarlo.
                                 */
                                // .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                                // }));
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .jwtAuthenticationConverter(
                                                                                jwtAuthenticationConverter())));

                return http.build();
        }

        @Bean // Spring Security dice que:
        PasswordEncoder passwordEncoder() {// Cuando manejemos contraseñas, utiliza BCrypt.
                return new BCryptPasswordEncoder();
        }

        @Bean
        UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
                /*
                 * Se le dice a Spring Security: Cuando necesites buscar a un usuario, búscalo
                 * en mi tabla app_user utilizando AppUserRepository
                 */
                return username -> appUserRepository
                                .findByUsernameAndActive(username, true)// busca el usuario cuyo username coincida y que
                                                                        // además esté
                                                                        // activo.
                                .map(user -> org.springframework.security.core.userdetails.User
                                                .withUsername(user.getUsername())
                                                .password(user.getPassword())
                                                .roles(user.getRole())
                                                .build())
                                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(
                                                "USUARIO NO ENCONTRADO: " + username));
        }

        @Bean
        AuthenticationManager authenticationManager(
                        AuthenticationConfiguration configuration) throws Exception {

                return configuration.getAuthenticationManager(); // Dame el AuthenticationManager que configuraste con
                // El metodo evita que lance: "Spring encontró que no existe ningún bean
                // AuthenticationManager" --No qualifying bean of type // mis mecanismos de
                // autenticación.
        }

        // transformar los claims del token en las autoridades que usará el sistema de
        // autorización
        @Bean //
        JwtAuthenticationConverter jwtAuthenticationConverter() {

                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

                authoritiesConverter.setAuthoritiesClaimName("role");
                authoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

                converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

                return converter;
        }
}