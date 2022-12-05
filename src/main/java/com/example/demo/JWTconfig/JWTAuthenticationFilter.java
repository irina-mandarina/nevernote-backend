package com.example.demo.JWTconfig;

import com.example.demo.Services.LoggedService;
import com.example.demo.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final LoggedService loggedService;

    private boolean isValid(String token) throws JSONException, IllegalArgumentException {

        String sub = getSub(token);
        int exp = getExp(token);

        // if not expired
        if ((exp) > (new Date().getTime() / 1000)) {
            // if the user is logged
            return loggedService.isLogged(userService.findByUsername(sub));
        }

        return false;
    }

    private String getSub(String token) throws JSONException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        JSONObject payload = new JSONObject(new String(decoder.decode(chunks[1])));
        return (String) payload.get("sub");
    }

    private int getExp(String token) throws JSONException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        JSONObject payload = new JSONObject(new String(decoder.decode(chunks[1])));
        return (int) payload.get("exp");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/auth/register")) {
            // no jwt needed
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeaderValue = request.getHeader("Authorization");
        String token;

        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
            System.out.println("token: " + token);
            boolean tokenIsValid;

            try {
                tokenIsValid = isValid(token);
            } catch (JSONException e) {
                System.out.println("JSONException: Problematic payload");
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException: Invalid payload");
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                System.out.println("Exception: Could not validate token");
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (tokenIsValid) {
                try {
                    System.out.println("JWT is valid. From: " + request.getRequestURI());
                    request.setAttribute("username", getSub(token));
                    System.out.println("attr for " + request.getRequestURI() + ": " + request.getAttribute("username"));
                    request.getRequestURI();
                } catch (JSONException e) {
                    System.out.println("IllegalArgumentException: Invalid payload - cannot extract sub");
                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED);
                }
                filterChain.doFilter(request, response);
                return;
            } else {
                System.out.println("JWT is invalid.");
            }
        }

        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED);

    }
}