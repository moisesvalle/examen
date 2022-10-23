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
import com.sv.examen.model.CcPuerto;
import com.sv.examen.repository.CcCiudadRepository;
import com.sv.examen.repository.CcPaisRepository;
import com.sv.examen.repository.CcPuertoRepository;
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
public class CcPuertoService {

    private static final String OK = "Ok";

    @Autowired
    private CcPuertoRepository ccPuertoRepository;

    @Autowired
    private CcPaisRepository ccPaisRepository;

    @Autowired
    private CcCiudadRepository ccCiudadRepository;

    public ResponseEntity findAllPuerto() throws Exception {
        String puertoes = new Gson().toJson(ccPuertoRepository.findAll());
        return ResponseEntity.ok().body(puertoes);
    }

    public ResponseEntity findPuertoByFilter(String jsonString) throws Exception {
        JsonObject jsonFilter = new Gson().fromJson(jsonString, JsonObject.class);
        if (jsonFilter == null || jsonFilter.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        String codigo = (jsonFilter.get("codigo").isJsonNull()) ? null : jsonFilter.get("codigo").getAsString();
        String nombre = (jsonFilter.get("nombre").isJsonNull()) ? null : jsonFilter.get("nombre").getAsString();
        String numeroMuelle = (jsonFilter.get("numeroMuelle").isJsonNull()) ? null : jsonFilter.get("numeroMuelle").getAsString();
        Long idpais = (jsonFilter.get("idpais").isJsonNull()) ? null : jsonFilter.get("idpais").getAsLong();
        Long idciudad = (jsonFilter.get("idciudad").isJsonNull()) ? null : jsonFilter.get("idciudad").getAsLong();
        String puertos = new Gson().toJson(ccPuertoRepository.findAllByFilter(codigo, nombre, numeroMuelle, idpais, idciudad));
        return ResponseEntity.ok().body(puertos);
    }

    public ResponseEntity savePuerto(String jsonString) throws Exception {
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
        if (json.get("numeroMuelle") == null || Strings.isEmpty(json.get("numeroMuelle").getAsString())) {
            throw new IllegalArgumentException("El numero de muelle enviado no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("telefono") == null || Strings.isEmpty(json.get("telefono").getAsString())) {
            throw new IllegalArgumentException("El telefono enviado no se envio o esta vacio");
        }
        if (json.get("codigoPostal") == null || Strings.isEmpty(json.get("codigoPostal").getAsString())) {
            throw new IllegalArgumentException("El codigo postal enviado no se envio o esta vacio");
        }
        if (json.get("email") == null || Strings.isEmpty(json.get("email").getAsString())) {
            throw new IllegalArgumentException("El correo enviado no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais enviado no se envio o esta vacio");
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
        CcPuerto vpuerto = new Gson().fromJson(json, CcPuerto.class);
        if (ccPuertoRepository.existeCodigoCcPuerto(vpuerto.getCodigo())) {
            throw new ValidationException("El codigo ya existe");
        }
        vpuerto.setPais(pais);
        vpuerto.setCiudad(ciudad);
        ccPuertoRepository.save(vpuerto);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El puerto se guardo con exito", OK));
    }

    public ResponseEntity updatePuerto(String jsonString) throws Exception {
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
        if (json.get("numeroMuelle") == null || Strings.isEmpty(json.get("numeroMuelle").getAsString())) {
            throw new IllegalArgumentException("El numero de muelle enviado no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("telefono") == null || Strings.isEmpty(json.get("telefono").getAsString())) {
            throw new IllegalArgumentException("El telefono enviado no se envio o esta vacio");
        }
        if (json.get("codigoPostal") == null || Strings.isEmpty(json.get("codigoPostal").getAsString())) {
            throw new IllegalArgumentException("El codigo postal enviado no se envio o esta vacio");
        }
        if (json.get("email") == null || Strings.isEmpty(json.get("email").getAsString())) {
            throw new IllegalArgumentException("El correo enviado no se envio o esta vacio");
        }
        if (json.get("idpais") == null || json.get("idpais").isJsonNull()) {
            throw new IllegalArgumentException("El pais enviado no se envio o esta vacio");
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
        CcPuerto puerto = ccPuertoRepository.findOnePuertoById(json.get("id").getAsInt());
        if (puerto == null || puerto.getId() == null) {
            throw new NotFoundException("El puerto ha editar no existe");
        }
        CcPuerto vpuerto = new Gson().fromJson(json, CcPuerto.class);
        if (ccPuertoRepository.existeCodigoCcPuerto(vpuerto.getCodigo(), vpuerto.getId())) {
            throw new ValidationException("El codigo ya existe");
        }
        puerto.setPais(pais);
        puerto.setCiudad(ciudad);
        puerto.setCodigo(vpuerto.getCodigo());
        puerto.setNombre(vpuerto.getNombre());
        puerto.setNumeroMuelle(vpuerto.getNumeroMuelle());
        puerto.setDireccion(vpuerto.getDireccion());
        puerto.setTelefono(vpuerto.getTelefono());
        puerto.setCodigoPostal(vpuerto.getCodigoPostal());
        puerto.setEmail(vpuerto.getEmail());
        ccPuertoRepository.save(puerto);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El puerto se actualizo con exito", OK));
    }

    public ResponseEntity deletePuerto(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        CcPuerto puerto = ccPuertoRepository.findOnePuertoById(json.get("id").getAsInt());
        if (puerto == null || puerto.getId() == null) {
            throw new NotFoundException("El puerto ha eliminar no existe");
        }
        if (ccPuertoRepository.existeCcPuertoEnTrMaritimo(puerto.getId())) {
            throw new ValidationException("El puerto ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccPuertoRepository.delete(puerto);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El puerto se elimino con exito", OK));
    }
}
