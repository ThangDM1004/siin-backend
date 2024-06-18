package com.example.exe202backend.services.Auth;

import com.example.exe202backend.dto.CreateUserDTO;
import com.example.exe202backend.mapper.UserMapper;
import com.example.exe202backend.models.Role;
import com.example.exe202backend.models.Token;
import com.example.exe202backend.models.TokenType;
import com.example.exe202backend.models.UserModel;
import com.example.exe202backend.repositories.TokenRepository;
import com.example.exe202backend.repositories.UserRepository;
import com.example.exe202backend.request.AuthenticationRequest;
import com.example.exe202backend.response.AuthenticationResponse;
import com.example.exe202backend.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserMapper userMapper;

    public ResponseEntity<ResponseObject> register(CreateUserDTO request) {
        if (!repository.findByEmail(request.getEmail()).isPresent()) {
            var user = UserModel.builder()
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .fullName(request.getFullName())
                    .dob(request.getDob())
                    .status(true)
                    .password(encoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            Optional<UserModel> saved = Optional.of(repository.save(user));
            saved.ifPresent(users -> {
                try {
                    String token = UUID.randomUUID().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
            AuthenticationResponse auth = AuthenticationResponse.builder()
                    .access_token(jwtToken)
                .refresh_token(refreshToken)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "Register successfully",
                    auth
            ));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                "Email already exist",
                null
        ));
    }
    public ResponseEntity<ResponseObject> authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        if(user.getStatus()){
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(user, jwtToken);
            AuthenticationResponse auth = AuthenticationResponse.builder()
                    .access_token(jwtToken)
                    .refresh_token(refreshToken)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "Login successfully",
                    auth
            ));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject(
                "Login failed",
                null
        ));
    }
    private void saveUserToken(UserModel user, String jwtToken) {
        var token = Token.builder()
                .users(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserModel user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public ResponseEntity<ResponseObject> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                    "",
                    ""
            ));
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                        "Token not exist",
                        authResponse
                        ));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                "Token not exist",
                ""
                ));
    }
    public ResponseEntity<ResponseObject> logout(String token) {
        var storedToken = tokenRepository.findByToken(token)
                .orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "Logout successfully",
                    ""
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                "Token not exist",
                ""
        ));
    }

}
