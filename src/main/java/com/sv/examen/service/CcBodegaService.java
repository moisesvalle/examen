/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcBodega;
import com.sv.examen.model.CcCiudad;
import com.sv.examen.model.CcPais;
import com.sv.examen.repository.CcBodegaRepository;
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
public class CcBodegaService {

    private static final String OK = "Ok";

    @Autowired
    private CcBodegaRepository ccBodegaRepository;

    @Autowired
    private CcPaisRepository ccPaisRepository;

    @Autowired
    private CcCiudadRepository ccCiudadRepository;

    public ResponseEntity findAllBodega() throws Exception {
        String bodegaes = new Gson().toJson(ccBodegaRepository.findAll());
        return ResponseEntity.ok().body(bodegaes);
    }

    public ResponseEntity findBodegaByFilter(String jsonString) throws Exception {
        JsonObject jsonFilter = new Gson().fromJson(jsonString, JsonObject.class);
        if (jsonFilter == null || jsonFilter.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        String codigo = (jsonFilter.get("codigo").isJsonNull()) ? null : jsonFilter.get("codigo").getAsString();
        String nombre = (jsonFilter.get("nombre").isJsonNull()) ? null : jsonFilter.get("nombre").getAsString();
        Long idpais = (jsonFilter.get("idpais").isJsonNull()) ? null : jsonFilter.get("idpais").getAsLong();
        Long idciudad = (jsonFilter.get("idciudad").isJsonNull()) ? null : jsonFilter.get("idciudad").getAsLong();
        String bodegas = new Gson().toJson(ccBodegaRepository.findAllByFilter(codigo, nombre, idpais, idciudad));
        return ResponseEntity.ok().body(bodegas);
    }

    public ResponseEntity saveBodega(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("codigo") == null || Strings.isEmpty(json.get("codigo").getAsString())) {
            throw new IllegalArgumentException("El codigo no se envio o esta vacio");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("telefono") == null || Strings.isEmpty(json.get("telefono").getAsString())) {
            throw new IllegalArgumentException("El telefono no se envio o esta vacio");
        }
        if (json.get("codigoPostal") == null || Strings.isEmpty(json.get("codigoPostal").getAsString())) {
            throw new IllegalArgumentException("El codigo postal no se envio o esta vacio");
        }
        if (json.get("email") == null || Strings.isEmpty(json.get("email").getAsString())) {
            throw new IllegalArgumentException("El correo no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais no se envio o esta vacio");
        }
        if (json.get("idciudad") == null || json.get("idciudad").isJsonNull()) {
            throw new IllegalArgumentException("La ciudad enviada no se envio o esta vacia");
        }
        CcPais pais = ccPaisRepository.findOneCcPaisById(json.get("idpais").getAsInt());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais enviado no existe");
        }
        CcCiudad ciudad = ccCiudadRepository.findOneCiudadById(json.get("idciudad").getAsInt());
        if (ciudad == null || ciudad.getId() == null) {
            throw new NotFoundException("La ciudad enviada no existe");
        }
        CcBodega vbodega = new Gson().fromJson(json, CcBodega.class);
        if (ccBodegaRepository.existeCodigoCcBodega(vbodega.getCodigo())) {
            throw new ValidationException("El codigo ya existe");
        }
        vbodega.setPais(pais);
        vbodega.setCiudad(ciudad);
        ccBodegaRepository.save(vbodega);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La bodega se guardo con exito", OK));
    }

    public ResponseEntity updateBodega(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        if (json.get("codigo") == null || Strings.isEmpty(json.get("codigo").getAsString())) {
            throw new IllegalArgumentException("El codigo no se envio o esta vacio");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("telefono") == null || Strings.isEmpty(json.get("telefono").getAsString())) {
            throw new IllegalArgumentException("El telefono no se envio o esta vacio");
        }
        if (json.get("codigoPostal") == null || Strings.isEmpty(json.get("codigoPostal").getAsString())) {
            throw new IllegalArgumentException("El codigo postal no se envio o esta vacio");
        }
        if (json.get("email") == null || Strings.isEmpty(json.get("email").getAsString())) {
            throw new IllegalArgumentException("El correo no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais no se envio o esta vacio");
        }
        if (json.get("idciudad") == null || json.get("idciudad").isJsonNull()) {
            throw new IllegalArgumentException("La ciudad enviada no se envio o esta vacia");
        }
        CcPais pais = ccPaisRepository.findOneCcPaisById(json.get("idpais").getAsInt());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais enviado no existe");
        }
        CcCiudad ciudad = ccCiudadRepository.findOneCiudadById(json.get("idciudad").getAsInt());
        if (ciudad == null || ciudad.getId() == null) {
            throw new NotFoundException("La ciudad enviada no existe");
        }
        CcBodega bodega = ccBodegaRepository.findOneBodegaById(json.get("id").getAsInt());
        if (bodega == null || bodega.getId() == null) {
            throw new NotFoundException("La bodega ha editar no existe");
        }
        CcBodega vbodega = new Gson().fromJson(json, CcBodega.class);
        if (ccBodegaRepository.existeCodigoCcBodega(vbodega.getCodigo(), vbodega.getId())) {
            throw new ValidationException("El codigo ya existe");
        }
        bodega.setPais(pais);
        bodega.setCiudad(ciudad);
        bodega.setCodigo(vbodega.getCodigo());
        bodega.setNombre(vbodega.getNombre());
        bodega.setDireccion(vbodega.getDireccion());
        bodega.setTelefono(vbodega.getTelefono());
        bodega.setCodigoPostal(vbodega.getCodigoPostal());
        bodega.setEmail(vbodega.getEmail());
        ccBodegaRepository.save(bodega);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La bodega se actualizo con exito", OK));
    }

    public ResponseEntity deleteBodega(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        CcBodega bodega = ccBodegaRepository.findOneBodegaById(json.get("id").getAsInt());
        if (bodega == null || bodega.getId() == null) {
            throw new NotFoundException("La bodega ha eliminar no existe");
        }
        if (ccBodegaRepository.existeCcBodegaEnTrTerrestre(bodega.getId())) {
            throw new ValidationException("La bodega ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccBodegaRepository.delete(bodega);
        return ResponseEntity.ok().body(RestUtils.serverResponse("La bodega se elimino con exito", OK));
    }

}
