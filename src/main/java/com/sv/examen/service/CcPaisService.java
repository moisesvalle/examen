/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
public class CcPaisService {

    private static final String OK = "Ok";
    
    @Autowired
    private CcPaisRepository ccPaisRepository;

    @Autowired
    private CcCiudadRepository ccCiudadRepository;

    public ResponseEntity findAllPais() throws Exception {
        String paises = new Gson().toJson(ccPaisRepository.findAll());
        return ResponseEntity.ok().body(paises);
    }

    public ResponseEntity savePais(String jsonString) throws Exception {
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
//        if (json.get("mapsesion") == null || json.get("mapsesion").getAsJsonObject().entrySet().isEmpty()) {
//            throw new IllegalArgumentException("Faltan datos de usuario");
//        }
        CcPais vpais = new Gson().fromJson(json, CcPais.class);
        if (ccPaisRepository.existeCodCcPais(vpais.getCodigo())) {
            throw new ValidationException("El codigo ya existe");
        }
        ccPaisRepository.save(vpais);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El pais se guardo con exito", OK));
    }

    public ResponseEntity updatePais(String jsonString) throws Exception {
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
//        if (json.get("mapsesion") == null || json.get("mapsesion").getAsJsonObject().entrySet().isEmpty()) {
//            throw new IllegalArgumentException("Faltan datos de usuario");
//        }
        CcPais vpais = new Gson().fromJson(json, CcPais.class);
        CcPais pais = ccPaisRepository.findOneCcPaisById(vpais.getId());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais ha editar no existe");
        }
        if (ccPaisRepository.existeCodCcPais(vpais.getCodigo(), vpais.getId())) {
            throw new ValidationException("El codigo ya existe");
        }
        pais.setCodigo(vpais.getCodigo());
        pais.setNombre(vpais.getNombre());
        ccPaisRepository.save(pais);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El pais se actualizo con exito", OK));
    }

    public ResponseEntity deletePais(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
//        if (json.get("mapsesion") == null || json.get("mapsesion").getAsJsonObject().entrySet().isEmpty()) {
//            throw new IllegalArgumentException("Faltan datos de usuario");
//        }
        CcPais pais = ccPaisRepository.findOneCcPaisById(json.get("id").getAsInt());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais ha eliminar no existe");
        }
        if (ccPaisRepository.existeCcPaisEnBodega(pais.getId()) || ccPaisRepository.existeCcPaisEnPuerto(pais.getId()) || ccPaisRepository.existeCcPaisEnCiudad(pais.getId())) {
            throw new ValidationException("El pais ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccCiudadRepository.deleteAllByPais(pais.getCodigo());
        ccPaisRepository.delete(pais);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El pais se elimino con exito", OK));
    }

}
