package com.jonas.visitflow.auth;

import com.jonas.visitflow.auth.dto.LoginRequest;
import com.jonas.visitflow.auth.dto.LoginResponse;
import com.jonas.visitflow.auth.dto.RegisterRequest;
import com.jonas.visitflow.jwt.JwtService;
import com.jonas.visitflow.model.User;
import com.jonas.visitflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder().username(request.getUsername()).email(request.getEmail()).password(encodedPassword).build();
        userRepository.save(user);
    }

}
