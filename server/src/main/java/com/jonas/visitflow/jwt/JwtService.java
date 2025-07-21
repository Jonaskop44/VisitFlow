package com.jonas.visitflow.jwt;

import com.jonas.visitflow.model.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

@Service
public class JwtService {

    private final String secret = "awd";

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24)) // 24 Stunden
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

}
