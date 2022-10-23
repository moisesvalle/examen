/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.security;

import com.sv.examen.model.SsUsuario;
import com.sv.examen.utils.JWTUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Moises
 */
@Component

public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JWTUtils jWTUtils;

    private BCryptPasswordEncoder passwordEncoder;
    
    @Setter
    @Getter
    private SsUsuario ssUsuario = new SsUsuario();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String clave = authentication.getCredentials().toString();
            passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(clave, ssUsuario.getClave())) {
                SecUser userDetails = new SecUser(ssUsuario.getUsuario(), ssUsuario.getClave());
                String token = jWTUtils.generarToken(userDetails);
                Authentication auth = new UsernamePasswordAuthenticationToken(token, token, new ArrayList<>());
                SecurityContext context = new SecurityContextImpl(auth);
                SecurityContextHolder.setContext(context);
                return auth;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(JwtAuthenticationProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
