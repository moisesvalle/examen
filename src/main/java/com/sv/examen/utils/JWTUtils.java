/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.utils;

import com.sv.examen.security.SecUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Moises
 */
@Component
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTUtils {

    @Value("${security.jwt.expiration}")
    private Long expiracion;
    
    @Value("${security.jwt.pass}")
    private String pass;

    private Key key;

    @PostConstruct
    public void init() {
        try {
            this.key = Keys.hmacShaKeyFor(pass.getBytes());
        } catch (Exception ex) {
            Logger.getLogger(JWTUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Claims getAllClaimsFromToken(String token) throws Exception {
        JwtParser jwtp = Jwts.parserBuilder().setSigningKey(key).build();
        if (jwtp.isSigned(token)) {
            return jwtp.parseClaimsJws(token).getBody();
        }
        return null;
    }

    public String getNombreUsuarioFromToken(String token) throws Exception {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getFechaExpiracionFromToken(String token) throws Exception {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private boolean isTokenExpirado(String token) throws Exception {
        final Date dateExpiration = getFechaExpiracionFromToken(token);
        return dateExpiration.before(new Date());
    }

    public String generarToken(SecUser user) throws Exception {
        final Date fechaCreacion = new Date();
        final Date fechaExpiracion = new Date(fechaCreacion.getTime() + expiracion * 1000);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(fechaCreacion)
                .setExpiration(fechaExpiracion)
                .compact();
    }

    public boolean validateToken(String token) throws Exception {
        return !isTokenExpirado(token);
    }

}
