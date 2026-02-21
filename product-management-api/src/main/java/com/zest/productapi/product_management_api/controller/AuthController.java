package com.zest.productapi.product_management_api.controller;

import com.zest.productapi.product_management_api.dto.AuthRequest;
import com.zest.productapi.product_management_api.dto.AuthResponse;
import com.zest.productapi.product_management_api.security.JwtService;
import com.zest.productapi.product_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        try {
            // Create default user if not exists
            userService.createDefaultUser();

            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Load UserDetails (not String)
            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

            // Generate tokens using UserDetails
            String token = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build());
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}