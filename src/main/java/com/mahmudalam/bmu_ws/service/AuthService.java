package com.mahmudalam.bmu_ws.service;

import com.mahmudalam.bmu_ws.dto.request.LoginRequest;
import com.mahmudalam.bmu_ws.dto.request.RegisterRequest;
import com.mahmudalam.bmu_ws.dto.response.AuthResponse;
import com.mahmudalam.bmu_ws.model.RefreshToken;
import com.mahmudalam.bmu_ws.model.User;
import com.mahmudalam.bmu_ws.repository.RefreshTokenRepository;
import com.mahmudalam.bmu_ws.repository.UserRepository;
import com.mahmudalam.bmu_ws.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public AuthResponse login(LoginRequest request) {
        try {
            String requestEmail = request.getEmail().toLowerCase();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestEmail, request.getPassword()));

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(requestEmail);

                // Delete existing refresh tokens for user
                refreshTokenRepository.deleteByUser(user);
                refreshTokenRepository.flush(); // <-- force delete to DB

                String accessToken = jwtService.generateAccessToken(user);
                String refreshTokenStr = jwtService.generateRefreshToken(user);

                RefreshToken refreshToken = RefreshToken.builder()
                        .token(refreshTokenStr)
                        .user(user)
                        .expiryDate(new Date(System.currentTimeMillis() + jwtService.getRefreshTokenValidity()))
                        .build();

                refreshTokenRepository.save(refreshToken);

                return new AuthResponse(true, accessToken, refreshTokenStr, null);
            }
        } catch (AuthenticationException e){
            return new AuthResponse(false,null, null,"Invalid credentials.");
        }

        return new AuthResponse(false, null, null, "Authentication failed.");
    }

    public AuthResponse register(RegisterRequest request) {
        try {
            String requestEmail = request.getEmail().toLowerCase();
            if(userRepository.existsByEmail(requestEmail)){
                return new AuthResponse(false, null, null, "Email already exists.");
            }

            User user = User.builder()
                    .email(requestEmail)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            userRepository.save(user);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshTokenStr = jwtService.generateRefreshToken(user);

            RefreshToken refreshToken = RefreshToken.builder()
                    .token(refreshTokenStr)
                    .user(user)
                    .expiryDate(new Date(System.currentTimeMillis() + jwtService.getRefreshTokenValidity()))
                    .build();

            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(true, accessToken, refreshTokenStr, null);

        } catch (Exception e) {
            return new AuthResponse(false, null, null, "User registration failed.");
        }
    }

    public AuthResponse refreshToken(String refreshTokenStr) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshTokenStr);

        if (tokenOpt.isEmpty() || !jwtService.isRefreshTokenValid(refreshTokenStr)) {
            return new AuthResponse(false, null, null, "Invalid or expired refresh token.");
        }

        User user = tokenOpt.get().getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(true, newAccessToken, null, null);
    }
}
