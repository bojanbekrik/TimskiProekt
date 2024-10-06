package com.example.parkup.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.parkup.data.entities.Administrator;
import com.example.parkup.data.entities.Guest;
import com.example.parkup.data.entities.RegisteredUser;
import com.example.parkup.data.entities.Worker;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;



@CrossOrigin
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        String className = principal.getClass().getName().split("\\.")[5];
        String email=null;
        String fullName=null;
        Integer id =null;
        Collection<? extends GrantedAuthority> roles= new ArrayList<>();
        System.out.println("principal " +  principal.getClass().getName());
        System.out.println("classname " + className);
        switch (className) {
            case "RegisteredUser" -> {
                RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
                fullName = user.getFirstName() + " " + user.getLastName();
                email = user.getEmail();
                roles = user.getAuthorities();
                id = user.getId();
            }
            case "Worker" -> {
                Worker user = (Worker) authentication.getPrincipal();
                email = user.getEmail();
                fullName = user.getFirstName() + " " + user.getLastName();
                roles = user.getAuthorities();
                id = user.getId();
            }
            case "Administrator" -> {
                Administrator user = (Administrator) authentication.getPrincipal();
                email = user.getEmail();
                fullName = user.getFirstName() + " " + user.getLastName();
                id = user.getId();
                roles = user.getAuthorities();
            }
            case "Guest" -> {
                Guest user = (Guest) authentication.getPrincipal();
                email = user.getEmail();
                fullName = "GuestUser";
                id = user.getId();
                roles = user.getAuthorities();
            }
        }
        System.out.println("fullName: " +  fullName);
        System.out.println("id: " +  id);
        System.out.println("roles: " + roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(email)
                .withClaim("fullName",fullName)
                .withClaim("id",id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
