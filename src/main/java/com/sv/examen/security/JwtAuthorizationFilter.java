/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.security;

import com.sv.examen.utils.JWTUtils;
import com.sv.examen.utils.RestExceptionHandler;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author Moises
 */
@Setter
@Getter
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JWTUtils jWTUtils;
    

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearer = "Bearer ";
        if (Objects.isNull(header) || !header.startsWith(bearer)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearer = "Bearer ";
        if (Objects.nonNull(header) && header.startsWith(bearer)) {
            try {
                String authToken = header.substring(bearer.length());
                Claims claims = jWTUtils.getAllClaimsFromToken(authToken);
                if (claims == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
                    return new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>());
                }
                return new UsernamePasswordAuthenticationToken(authToken, authToken, new ArrayList<>());
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
                return new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>());
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
            return new UsernamePasswordAuthenticationToken(null, null, new ArrayList<>());
        }
    }

}
