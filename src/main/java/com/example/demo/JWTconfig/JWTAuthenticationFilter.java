package com.example.demo.JWTconfig;

import com.example.demo.Services.LoggedService;
import com.example.demo.Services.UserService;
import com.example.demo.Services.UserServiceImpl;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RequiredArgsConstructor
//@NoArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWT jwt = new JWT();

    public JWTAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/register")) {
            // no jwt needed
            filterChain.doFilter(request, response);
        }

        String authorizationHeaderValue = request.getHeader("Authorization");
        System.out.println(authorizationHeaderValue);
        String token = null;

        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
            System.out.println(token);
            if (jwt.isValid(token)) {
                System.out.println("JWT is valid.");
                filterChain.doFilter(request, response);
            }
        }
        else {
            System.out.println("JWT is invalid.");
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}