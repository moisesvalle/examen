/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcCiudad;
import com.sv.examen.model.CcPais;
import com.sv.examen.repository.CcCiudadRepository;
import com.sv.examen.repository.CcPaisRepository;
import com.sv.examen.utils.RestUtils;
import javassist.NotFoundException;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author Moises
 */
@Service
@Slf4j
@Transactional
public class CcCiudadService {

    private static final String OK = "Ok";

    @Autowired
    private CcCiudadRepository ccCiudadRepository;

    @Autowired
    private CcPaisRepository ccPaisRepository;

    public ResponseEntity findAllByPais(Integer idpais) throws Exception {
        String ciudads = new Gson().toJson(ccCiudadRepository.findByPais(idpais));
        return ResponseEntity.ok().body(ciudads);
    }

    public ResponseEntity saveCiudad(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("codigo") == null || Strings.isEmpty(json.get("codigo").getAsString())) {
            throw new IllegalArgumentException("El codigo enviado no se envio o esta vacio");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre enviado no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais enviado no se envio o esta vacio");
        }
        CcCiudad vciudad = new Gson().fromJson(json, CcCiudad.class);
        if (ccCiudadRepository.existeCodCiudad(vciudad.getCodigo())) {
            throw new ValidationException("El codigo ya existe");
        }
        CcPais pais = new CcPais();
        pais.setId(json.get("idpais").getAsInt());
        vciudad.setPais(pais);
        ccCiudadRepository.save(vciudad);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La ciudad se guardo con exito", OK));
    }

    public ResponseEntity updateCiudad(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        if (json.get("codigo") == null || Strings.isEmpty(json.get("codigo").getAsString())) {
            throw new IllegalArgumentException("El codigo enviado no se envio o esta vacio");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre enviado no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais enviado no se envio o esta vacio");
        }
        CcPais pais = ccPaisRepository.findOneCcPaisById(json.get("idpais").getAsInt());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais enviado no existe");
        }
        CcCiudad ciudad = ccCiudadRepository.findOneCiudadById(json.get("id").getAsInt());
        if (ciudad == null || ciudad.getId() == null) {
            throw new NotFoundException("La ciudad ha editar no existe");
        }
        CcCiudad vciudad = new Gson().fromJson(json, CcCiudad.class);
        if (ccCiudadRepository.existeCodCiudad(vciudad.getCodigo(), vciudad.getId())) {
            throw new ValidationException("El codigo ya existe");
        }
        vciudad.setPais(pais);
        ciudad.setNombre(vciudad.getNombre());
        ciudad.setPais(vciudad.getPais());
        ccCiudadRepository.save(ciudad);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La ciudad se actualizo con exito", OK));
    }

    public ResponseEntity deleteCiudad(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }

        CcCiudad ciudad = ccCiudadRepository.findOneCiudadById(json.get("id").getAsInt());
        if (ciudad == null || ciudad.getId() == null) {
            throw new NotFoundException("La ciudad ha eliminar no existe");
        }
        if (ccCiudadRepository.existeCcCiudadEnBodega(ciudad.getId()) || ccCiudadRepository.existeCcCiudadEnPuerto(ciudad.getId())) {
            throw new ValidationException("La ciudad ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccCiudadRepository.delete(ciudad);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La ciudad se elimino con exito", OK));
    }
}
