package com.jara.employee_management.service;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.jara.employee_management.model.request.LoginRequest;
import com.jara.employee_management.model.response.LoginResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("employee-management-api")
                .subject(request.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("role", "ADMIN")
                .build();

        String token = jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                JwsHeader.with(
                                        SignatureAlgorithm.RS256)
                                        .build(),
                                claims))
                .getTokenValue();

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .build();
    }
}