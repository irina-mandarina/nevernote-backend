package com.example.demo.security;

import com.example.demo.services.AuthorityService;
import com.example.demo.types.AuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class RoleFilter extends OncePerRequestFilter {

    private final AuthorityService authorityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ((request.getRequestURI().contains("roles") && !request.getRequestURI().equals("/auth/roles"))
                || request.getRequestURI().contains("history/search") ) {
            if (!authorityService.hasRole(request.getAttribute("username").toString(), AuthorityType.ADMIN)) {
                System.out.println(request.getAttribute("username").toString() + " does not have an admin role");
                response.setStatus(
                        HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
