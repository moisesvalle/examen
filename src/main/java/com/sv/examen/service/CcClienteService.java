/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcCiudad;
import com.sv.examen.model.CcCliente;
import com.sv.examen.model.CcPais;
import com.sv.examen.repository.CcCiudadRepository;
import com.sv.examen.repository.CcClienteRepository;
import com.sv.examen.repository.CcPaisRepository;
import com.sv.examen.utils.RestUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class CcClienteService {

    private static final String OK = "Ok";

    @Autowired
    private CcClienteRepository ccClienteRepository;

    @Autowired
    private CcPaisRepository ccPaisRepository;

    @Autowired
    private CcCiudadRepository ccCiudadRepository;

    public ResponseEntity findAllCliente() throws Exception {
        String clientes = new Gson().toJson(ccClienteRepository.findAll());
        return ResponseEntity.ok().body(clientes);
    }

    public ResponseEntity saveCliente(String jsonString) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create(); 
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre enviado no se envio o esta vacio");
        }
        if (json.get("apellido") == null || Strings.isEmpty(json.get("apellido").getAsString())) {
            throw new IllegalArgumentException("El apellido enviado no se envio o esta vacio");
        }
        if (json.get("sexo") == null || Strings.isEmpty(json.get("sexo").getAsString())) {
            throw new IllegalArgumentException("El sexo enviado no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("fechaNac") == null || Strings.isEmpty(json.get("fechaNac").getAsString())) {
            throw new IllegalArgumentException("La fecha de nacimiento enviada no se envio o esta vacia");
        }
        if (!json.get("fechaNac").getAsString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            throw new ValidationException("El formato de la fecha de nacimiento es incorrecto (dd/MM/yyyy)");
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
            throw new NotFoundException("La ciudad enviada no se envio o esta vacia");
        }
        CcPais pais = ccPaisRepository.findOneCcPaisById(json.get("idpais").getAsInt());
        if (pais == null || pais.getId() == null) {
            throw new NotFoundException("El pais enviado no existe");
        }
        CcCiudad ciudad = ccCiudadRepository.findOneCiudadById(json.get("idciudad").getAsInt());
        if (ciudad == null || ciudad.getId() == null) {
            throw new NotFoundException("La ciudad enviada no existe");
        }

        CcCliente cliente = gson.fromJson(json, CcCliente.class);
        cliente.setPais(pais);
        cliente.setCiudad(ciudad);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = formatter.parse(json.get("fechaNac").getAsString());
        cliente.setFechaNac(fecha);
        ccClienteRepository.save(cliente);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El cliente se guardo con exito", OK));
    }

    public ResponseEntity updateCliente(String jsonString) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create(); 
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        if (json.get("nombre") == null || Strings.isEmpty(json.get("nombre").getAsString())) {
            throw new IllegalArgumentException("El nombre enviado no se envio o esta vacio");
        }
        if (json.get("apellido") == null || Strings.isEmpty(json.get("apellido").getAsString())) {
            throw new IllegalArgumentException("El apellido enviado no se envio o esta vacio");
        }
        if (json.get("sexo") == null || Strings.isEmpty(json.get("sexo").getAsString())) {
            throw new IllegalArgumentException("El sexo enviado no se envio o esta vacio");
        }
        if (json.get("direccion") == null || Strings.isEmpty(json.get("direccion").getAsString())) {
            throw new IllegalArgumentException("La direccion enviada no se envio o esta vacia");
        }
        if (json.get("fechaNac") == null || Strings.isEmpty(json.get("fechaNac").getAsString())) {
            throw new IllegalArgumentException("La fecha de nacimiento enviada no se envio o esta vacia");
        }
        if (!json.get("fechaNac").getAsString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            throw new ValidationException("El formato de la fecha de nacimiento es incorrecto (dd/MM/yyyy)");
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
        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("id").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente ha editar no existe");
        }
        CcCliente vcliente = gson.fromJson(json, CcCliente.class);
        cliente.setPais(pais);
        cliente.setCiudad(ciudad);
        cliente.setNombre(vcliente.getNombre());
        cliente.setApellido(vcliente.getApellido());
        cliente.setSexo(vcliente.getSexo());
        cliente.setDireccion(vcliente.getDireccion());
        cliente.setTelefono(vcliente.getTelefono());
        cliente.setCodigoPostal(vcliente.getCodigoPostal());
        cliente.setEmail(vcliente.getEmail());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = formatter.parse(json.get("fechaNac").getAsString());
        cliente.setFechaNac(fecha);
        ccClienteRepository.save(cliente);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El cliente se actualizo con exito", OK));
    }

    public ResponseEntity deleteCliente(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }

        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("id").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente ha eliminar no existe");
        }
        if (ccClienteRepository.existeCcClienteEnTrMaritimo(cliente.getId()) || ccClienteRepository.existeCcClienteEnTrTerrestre(cliente.getId())) {
            throw new ValidationException("El cliente ha eliminar ya esta siendo usado en areas del sistema");
        }
        ccClienteRepository.delete(cliente);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El cliente se elimino con exito", OK));
    }
}
