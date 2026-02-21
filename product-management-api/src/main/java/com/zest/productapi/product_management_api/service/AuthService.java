package com.zest.productapi.product_management_api.service;

import com.zest.productapi.product_management_api.dto.AuthRequest;
import com.zest.productapi.product_management_api.dto.AuthResponse;
import com.zest.productapi.product_management_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthResponse authenticate(AuthRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Load UserDetails (NOT String)
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

        // Generate tokens using UserDetails
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}