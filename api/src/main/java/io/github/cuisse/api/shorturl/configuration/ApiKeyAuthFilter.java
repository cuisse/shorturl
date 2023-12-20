package io.github.cuisse.api.shorturl.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${api.key}")
    private String key;

    @Value("${api.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader(key) != null) {
            if (secret.equals(request.getHeader(key))) {
                SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static final Authentication AUTHENTICATION = new AbstractAuthenticationToken(List.of()) {
            @Override
            public Object getCredentials()   { return null; }
            @Override
            public boolean isAuthenticated() { return true; }
            @Override
            public Object getPrincipal()     { return null; }
    };

}
