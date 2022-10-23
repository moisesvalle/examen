/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sv.examen.model.SsUsuario;
import com.sv.examen.repository.SsUsuarioRepository;
import com.sv.examen.security.JwtAuthenticationProvider;
import com.sv.examen.utils.RestUtils;
import java.util.ArrayList;
import javassist.NotFoundException;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author Moises
 */
@Service
@Transactional
@Slf4j
public class SsUsuarioService {

    private static final String OK = "Ok";

    @Autowired
    private SsUsuarioRepository ssUsuarioRepository;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity findByUser(String jsonString) throws Exception {
        JsonObject jsonFilter = new Gson().fromJson(jsonString, JsonObject.class);
        if (jsonFilter == null || jsonFilter.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos enviados son incorrectos");
        }
        String usuario = (jsonFilter.get("usuario") == null) ? "" : jsonFilter.get("usuario").getAsString();
        String clave = (jsonFilter.get("clave") == null) ? "" : jsonFilter.get("clave").getAsString();

        SsUsuario ssUsuario = ssUsuarioRepository.findOneSsUsuarioByUsuario(usuario);
        if (ssUsuario == null || ssUsuario.getId() == null) {
            throw new AuthenticationCredentialsNotFoundException("Acceso no autorizado");
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso no autorizado");
        }
        jwtAuthenticationProvider.setSsUsuario(ssUsuario);
        Authentication authentication = jwtAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(usuario, clave, new ArrayList<>()));
        if (authentication != null && authentication.getName() != null) {
            return ResponseEntity.ok().body(RestUtils.serverResponse(authentication.getName(), OK));
        } else {
            throw new AuthenticationCredentialsNotFoundException("Acceso no autorizado");
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso no autorizado");
        }
    }

    public ResponseEntity saveUsuario(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("usuario") == null || Strings.isEmpty(json.get("usuario").getAsString())) {
            throw new IllegalArgumentException("El usuario enviado no se envio o esta vacio");
        }
        if (json.get("clave") == null || Strings.isEmpty(json.get("clave").getAsString())) {
            throw new IllegalArgumentException("La clave enviada no se envio o esta vacio");
        }
        SsUsuario ssUsuario = new Gson().fromJson(json, SsUsuario.class);
        ssUsuario.setUsuario(StringUtils.trimAllWhitespace(ssUsuario.getUsuario()));
        if (ssUsuarioRepository.existeSsUsuario(ssUsuario.getUsuario())) {
            throw new ValidationException("Este usuario ya existe");
        }
        passwordEncoder = new BCryptPasswordEncoder();
        ssUsuario.setClave(passwordEncoder.encode(ssUsuario.getClave()));
        ssUsuarioRepository.save(ssUsuario);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El usuario se guardo con exito", OK));
    }

    public ResponseEntity cambioClave(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("usuario") == null || Strings.isEmpty(json.get("usuario").getAsString())) {
            throw new IllegalArgumentException("El usuario enviado no se envio o esta vacio");
        }
        if (json.get("claveActual") == null || Strings.isEmpty(json.get("claveActual").getAsString())) {
            throw new IllegalArgumentException("La clave actual enviada no se envio o esta vacia");
        }
        if (json.get("clave") == null || Strings.isEmpty(json.get("clave").getAsString())) {
            throw new IllegalArgumentException("La clave enviada no se envio o esta vacia");
        }
        SsUsuario vssUsuario = new Gson().fromJson(json, SsUsuario.class);
        SsUsuario ssUsuario = ssUsuarioRepository.findOneSsUsuarioByUsuario(json.get("usuario").getAsString());
        if (ssUsuario == null || ssUsuario.getId() == null) {
            throw new NotFoundException("El usuario ha editar no existe");
        }
        passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(json.get("usuario").getAsString(), ssUsuario.getClave())) {
            throw new ValidationException("La clave actual es incorrecta");
        }
        ssUsuario.setClave(passwordEncoder.encode(vssUsuario.getClave()));
        ssUsuarioRepository.save(ssUsuario);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El usuario se actualizo con exito", OK));
    }

}
