/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcBodega;
import com.sv.examen.model.CcCliente;
import com.sv.examen.model.CcTipoProducto;

import com.sv.examen.model.TrTerrestre;
import com.sv.examen.repository.CcBodegaRepository;
import com.sv.examen.repository.CcClienteRepository;
import com.sv.examen.repository.CcTipoProductoRepository;
import com.sv.examen.repository.TrTerrestreRepository;
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
public class TrTerrestreService {

    private static final String OK = "Ok";

    @Autowired
    private TrTerrestreRepository trTerrrestreRepository;

    @Autowired
    private CcBodegaRepository ccBodegaRepository;

    @Autowired
    private CcTipoProductoRepository ccTipoProductoRepository;

    @Autowired
    private CcClienteRepository ccClienteRepository;

    public ResponseEntity findAllTerrestre() throws Exception {
        String terrestrees = new Gson().toJson(trTerrrestreRepository.findAll());
        return ResponseEntity.ok().body(terrestrees);
    }

    public ResponseEntity findTerrestreByFilter(String jsonString) throws Exception {
        JsonObject jsonFilter = new Gson().fromJson(jsonString, JsonObject.class);
        if (jsonFilter == null || jsonFilter.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        String placavehiculo = (jsonFilter.get("placavehiculo").isJsonNull()) ? null : jsonFilter.get("placavehiculo").getAsString();
        String numeroguia = (jsonFilter.get("numeroguia").isJsonNull()) ? null : jsonFilter.get("numeroguia").getAsString();
        Long idbodega = (jsonFilter.get("idbodega").isJsonNull()) ? null : jsonFilter.get("idbodega").getAsLong();
        Long idtipoProducto = (jsonFilter.get("idtipoProducto").isJsonNull()) ? null : jsonFilter.get("idtipoProducto").getAsLong();
        Long idcliente = (jsonFilter.get("idcliente").isJsonNull()) ? null : jsonFilter.get("idcliente").getAsLong();
        String terrestres = new Gson().toJson(trTerrrestreRepository.findAllByFilter(placavehiculo, numeroguia, idbodega, idtipoProducto, idcliente));
        return ResponseEntity.ok().body(terrestres);
    }

    public ResponseEntity saveTerrestre(String jsonString) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("cantidadProducto") == null || json.get("cantidadProducto").isJsonNull()) {
            throw new IllegalArgumentException("La cantidad enviada no se envio o esta vacia");
        }
        if (json.get("fechaRegistro") == null || Strings.isEmpty(json.get("fechaRegistro").getAsString())) {
            throw new IllegalArgumentException("La fecha de registro enviada no se envio o esta vacia");
        }
        if (!json.get("fechaRegistro").getAsString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            throw new ValidationException("El formato de la fecha de registro es incorrecto (dd/MM/yyyy)");
        }
        if (json.get("fechaEntrega") == null || Strings.isEmpty(json.get("fechaEntrega").getAsString())) {
            throw new IllegalArgumentException("La fecha de entrega enviada no se envio o esta vacia");
        }
        if (!json.get("fechaEntrega").getAsString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            throw new ValidationException("El formato de la fecha de entrega es incorrecto (dd/MM/yyyy)");
        }
        if (json.get("precio") == null || json.get("precio").isJsonNull()) {
            throw new IllegalArgumentException("El precio enviado no se envio o esta vacio");
        }
        if (json.get("placaVehiculo") == null || Strings.isEmpty(json.get("placaVehiculo").getAsString())) {
            throw new IllegalArgumentException("La placa de vehiculo enviada no se envio o esta vacia");
        }
        if (json.get("placaVehiculo").getAsString().length() != 6 || !json.get("placaVehiculo").getAsString().matches("^[A-Za-z]{3}[0-9]{3}")) {
            throw new ValidationException("El formato de placa no es correcto");
        }
        if (json.get("numeroGuia") == null || Strings.isEmpty(json.get("numeroGuia").getAsString())) {
            throw new IllegalArgumentException("El numero de guia enviado no se envio o esta vacio");
        }
        if (json.get("numeroGuia").getAsString().length() != 10 || !json.get("numeroGuia").getAsString().matches("^[A-Za-z0-9]{10}")) {
            throw new ValidationException("El formato del numero guia no es correcto");
        }

        if (json.get("idbodega") == null || json.get("idbodega").isJsonNull()) {
            throw new IllegalArgumentException("El bodega enviado no se envio o esta vacia");
        }
        if (json.get("idtipoProducto") == null || json.get("idtipoProducto").isJsonNull()) {
            throw new IllegalArgumentException("El tipo de producto enviado  no se envio o esta vacio");
        }
        if (json.get("idcliente") == null || json.get("idcliente").isJsonNull()) {
            throw new IllegalArgumentException("El cliente enviado no se envio o esta vacio");
        }
        CcBodega bodega = ccBodegaRepository.findOneBodegaById(json.get("idbodega").getAsInt());
        if (bodega == null || bodega.getId() == null) {
            throw new NotFoundException("El bodega enviado no existe");
        }
        CcTipoProducto tipoProducto = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("idtipoProducto").getAsInt());
        if (tipoProducto == null || tipoProducto.getId() == null) {
            throw new NotFoundException("El tipo de producto enviado no existe");
        }
        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("idcliente").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente enviado no existe");
        }
        TrTerrestre vterrestre = gson.fromJson(json, TrTerrestre.class);
        if (trTerrrestreRepository.existeNumeroGuiaTrTerrestre(vterrestre.getNumeroGuia())) {
            throw new ValidationException("El numero guia ya existe");
        }

        Integer cantidad = json.get("cantidadProducto").getAsInt();
        Double precio = json.get("precio").getAsDouble();
        Double descuento = (cantidad > 10) ? 0.05 : 0.00;
        Double precioTotal = (precio - (precio * descuento));
        vterrestre.setPrecioNormal(precio);
        vterrestre.setDescuento(descuento);
        vterrestre.setPrecioEnvio(precioTotal);
        vterrestre.setBodega(bodega);
        vterrestre.setTipoProducto(tipoProducto);
        vterrestre.setCliente(cliente);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaRegistro = formatter.parse(json.get("fechaRegistro").getAsString());
        vterrestre.setFechaRegistro(fechaRegistro);
        Date fechaEntrega = formatter.parse(json.get("fechaEntrega").getAsString());
        vterrestre.setFechaEntrega(fechaEntrega);
        trTerrrestreRepository.save(vterrestre);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte terrestre se guardo con exito", OK));
    }

    public ResponseEntity updateTerrestre(String jsonString) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        if (json.get("cantidadProducto") == null || json.get("cantidadProducto").isJsonNull()) {
            throw new IllegalArgumentException("La cantidad enviada no se envio o esta vacia");
        }
        if (json.get("fechaEntrega") == null || Strings.isEmpty(json.get("fechaEntrega").getAsString())) {
            throw new IllegalArgumentException("La fecha de entrega enviada no se envio o esta vacia");
        }
        if (!json.get("fechaEntrega").getAsString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            throw new ValidationException("El formato de la fecha de entrega es incorrecto (dd/MM/yyyy)");
        }
        if (json.get("precio") == null || json.get("precio").isJsonNull()) {
            throw new IllegalArgumentException("El precio enviado no se envio o esta vacio");
        }
        if (json.get("placaVehiculo") == null || Strings.isEmpty(json.get("placaVehiculo").getAsString())) {
            throw new IllegalArgumentException("La placa de vehiculo enviada no se envio o esta vacia");
        }
        if (json.get("placaVehiculo").getAsString().length() != 6 || !json.get("placaVehiculo").getAsString().matches("^[A-Za-z]{3}[0-9]{3}")) {
            throw new ValidationException("El formato de placa no es correcto");
        }
        if (json.get("numeroGuia") == null || Strings.isEmpty(json.get("numeroGuia").getAsString())) {
            throw new IllegalArgumentException("El numero de guia enviado no se envio o esta vacio");
        }
        if (json.get("numeroGuia").getAsString().length() != 10 || !json.get("numeroGuia").getAsString().matches("^[A-Za-z0-9]{10}")) {
            throw new ValidationException("El formato del numero guia no es correcto");
        }

        if (json.get("idbodega") == null || json.get("idbodega").isJsonNull()) {
            throw new IllegalArgumentException("El bodega enviado no se envio o esta vacia");
        }
        if (json.get("idtipoProducto") == null || json.get("idtipoProducto").isJsonNull()) {
            throw new IllegalArgumentException("El tipo de producto enviado  no se envio o esta vacio");
        }
        if (json.get("idcliente") == null || json.get("idcliente").isJsonNull()) {
            throw new IllegalArgumentException("El cliente enviado no se envio o esta vacio");
        }
        CcBodega bodega = ccBodegaRepository.findOneBodegaById(json.get("idbodega").getAsInt());
        if (bodega == null || bodega.getId() == null) {
            throw new NotFoundException("El bodega enviado no existe");
        }
        CcTipoProducto tipoProducto = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("idtipoProducto").getAsInt());
        if (tipoProducto == null || tipoProducto.getId() == null) {
            throw new NotFoundException("El tipo de producto enviado no existe");
        }
        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("idcliente").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente enviado no existe");
        }
        TrTerrestre terrestre = trTerrrestreRepository.findOneTerrestreById(json.get("id").getAsInt());
        if (terrestre == null || terrestre.getId() == null) {
            throw new NotFoundException("El transporte terrestre ha editar no existe");
        }
        TrTerrestre vterrestre = gson.fromJson(json, TrTerrestre.class);
        if (trTerrrestreRepository.existeNumeroGuiaTrTerrestre(vterrestre.getNumeroGuia(), vterrestre.getId())) {
            throw new ValidationException("El numero guia ya existe");
        }
        if (vterrestre.getPlacaVehiculo().length() != 6 || !vterrestre.getPlacaVehiculo().matches("^[A-Za-z]{3}[0-9]{3}")) {
            throw new IllegalArgumentException("El formato de placa no es correcto");
        }
        Integer cantidad = json.get("cantidadProducto").getAsInt();
        Double precio = json.get("precio").getAsDouble();
        Double descuento = (cantidad > 10) ? 0.05 : 0.00;
        Double precioTotal = (precio - (precio * descuento));
        terrestre.setBodega(bodega);
        terrestre.setTipoProducto(tipoProducto);
        terrestre.setCliente(cliente);
        terrestre.setPrecioNormal(precio);
        terrestre.setDescuento(descuento);
        terrestre.setPrecioEnvio(precioTotal);
        terrestre.setCantidadProducto(vterrestre.getCantidadProducto());
        terrestre.setFechaRegistro(vterrestre.getFechaRegistro());
        terrestre.setFechaEntrega(vterrestre.getFechaEntrega());
        terrestre.setPlacaVehiculo(vterrestre.getPlacaVehiculo());
        terrestre.setNumeroGuia(vterrestre.getNumeroGuia());

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaEntrega = formatter.parse(json.get("fechaEntrega").getAsString());
        terrestre.setFechaEntrega(fechaEntrega);
        trTerrrestreRepository.save(terrestre);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte terrestre se actualizo con exito", OK));
    }

    public ResponseEntity deleteTerrestre(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        TrTerrestre terrestre = trTerrrestreRepository.findOneTerrestreById(json.get("id").getAsInt());
        if (terrestre == null || terrestre.getId() == null) {
            throw new NotFoundException("El transporte terrestre ha eliminar no existe");
        }
        trTerrrestreRepository.delete(terrestre);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte terrestre se elimino con exito", OK));
    }
}
