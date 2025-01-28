package com.learning.__security.configuration.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.learning.__security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

// con esto haces que se haga una vez por peticion
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null) { // token viene con BEARER jdslñfasdhlk12334rfsd
            jwtToken = jwtToken.substring(7); // Quita el bearer + 'espacio'
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            String username = jwtUtils.extractUsername(decodedJWT);
            // recogemos las authorities
            String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            // convierte el string de authorities separadas por coma a Lista de GrantedAuthority
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
