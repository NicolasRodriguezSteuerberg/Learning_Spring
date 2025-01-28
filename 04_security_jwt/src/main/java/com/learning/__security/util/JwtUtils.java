package com.learning.__security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.key.private}")
    private String privateKey;

    @Value("${jwt.user.generator}")
    private String userGenerator;

    // las propiedades de los jwTokens son CLAIMS
    // tiene 3, HEADER, PLAYLOAD, VERIFY SIGNATURE

    public String createToken(Authentication authentication) {
        // definimos el algoritmo
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        // Recogemos el nombre del usuario
        String username = authentication.getPrincipal().toString();
        // recogemos las autoridades de la siguiente forma
        // READ, WRITE, CREATE
        String authorities = authentication.getAuthorities()
                .stream()
                //.map(grantedAuthority -> grantedAuthority.getAuthority()) // lo mismo que abajo
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        // READ;
        return JWT.create()
                // creamos el token con el usuario x
                .withIssuer(userGenerator)
                // para el usuario x
                .withSubject(username)
                // añadimos un claim nuevo al token?
                .withClaim("authorities", authorities)
                // le ponemos el inicio y la finalización del token
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                // despues de cuando este token va a considerarse valido
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

}
