/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcTipoProducto;
import com.sv.examen.repository.CcTipoProductoRepository;
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
public class CcTipoProductoService {

    private static final String OK = "Ok";

    @Autowired
    private CcTipoProductoRepository ccTipoProductoRepository;

    public ResponseEntity findAllCcTipoProducto() throws Exception {
        String tiposEstudios = new Gson().toJson(ccTipoProductoRepository.findAll());
        return ResponseEntity.ok().body(tiposEstudios);
    }

    public ResponseEntity saveCcTipoProducto(String jsonString) throws Exception {
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
        if (ccTipoProductoRepository.existeCodigoCcTipoProducto(json.get("codigo").getAsString())) {
            throw new ValidationException("El codigo ya existe");
        }
        CcTipoProducto vtipo = new Gson().fromJson(json, CcTipoProducto.class);
        ccTipoProductoRepository.save(vtipo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El tipo de producto se guardo con exito", OK));
    }

    public ResponseEntity updateCcTipoProducto(String jsonString) throws Exception {
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

        CcTipoProducto vtipo = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("id").getAsInt());
        if (vtipo == null || vtipo.getId() == null) {
            throw new NotFoundException("El tipo de producto ha editar no existe");
        }
        if (ccTipoProductoRepository.existeCodigoCcTipoProducto(json.get("codigo").getAsString(), vtipo.getId())) {
            throw new ValidationException("El codigo ya existe");
        }
        vtipo.setNombre(json.get("codigo").getAsString());
        vtipo.setNombre(json.get("nombre").getAsString());
        ccTipoProductoRepository.save(vtipo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El tipo de producto se actualizo con exito", OK));
    }

    public ResponseEntity deleteCcTipoProducto(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        CcTipoProducto vtipo = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("id").getAsInt());
        if (vtipo == null || vtipo.getId() == null) {
            throw new NotFoundException("El tipo de producto ha eliminar no existe");
        }
        if (ccTipoProductoRepository.existeCcTipoProductoEnTrMaritimo(vtipo.getId()) || ccTipoProductoRepository.existeCcTipoProductoEnTrTerrestre(vtipo.getId())) {
            throw new ValidationException("El tipo de producto ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccTipoProductoRepository.delete(vtipo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El tipo de producto se elimino con exito", OK));
    }

}
