/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.security;

import com.sv.examen.model.SsUsuario;
import com.sv.examen.repository.SsUsuarioRepository;
import com.sv.examen.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Moises
 */
@Setter
@Getter
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    @Autowired
    private JWTUtils jWTUtils;
    
    @Autowired
    private SsUsuarioRepository ssUsuarioRepository;
    
    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    
    public JwtAuthenticationFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String bearer = "Bearer ";
            String authToken = null;
            Claims claims = null;
            if (header != null && header.startsWith(bearer)) {
                authToken = header.substring(bearer.length());
                claims = jWTUtils.getAllClaimsFromToken(authToken);
                if (claims == null) {
                    throw new ServletException("registro no encontrado");
                }
            } else {
                logger.warn("couldn't find bearer string, will ignore the header");
            }
            if (claims != null && claims.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                SsUsuario user = ssUsuarioRepository.findOneSsUsuarioByUsuario(claims.getSubject());
                if (user != null && jWTUtils.validateToken(user.getUsuario())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authToken, authToken, new ArrayList<>());
                    setDetails(request, authentication);
                    Authentication autenticacion = super.getAuthenticationManager().authenticate(authentication);
                    logger.info("authenticated user " + user.getUsuario() + ", setting security context");
                    SecurityContext context = new SecurityContextImpl(autenticacion);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JwtAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.setAuthenticationSuccessHandler(successHandler);
        super.successfulAuthentication(request, response, chain, authResult);
    }
    
}
