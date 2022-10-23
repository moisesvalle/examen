/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sv.examen.model.CcCliente;
import com.sv.examen.model.CcPuerto;
import com.sv.examen.model.CcTipoProducto;
import com.sv.examen.model.TrMaritimo;
import com.sv.examen.repository.CcClienteRepository;
import com.sv.examen.repository.CcPuertoRepository;
import com.sv.examen.repository.CcTipoProductoRepository;
import com.sv.examen.repository.TrMaritimoRepository;
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
public class TrMaritimoService {

    private static final String OK = "Ok";
    
    private static final String ERROR = "Error";

    @Autowired
    private TrMaritimoRepository trMaritimoRepository;

    @Autowired
    private CcPuertoRepository ccPuertoRepository;

    @Autowired
    private CcTipoProductoRepository ccTipoProductoRepository;

    @Autowired
    private CcClienteRepository ccClienteRepository;

    public ResponseEntity findAllMaritimo() throws Exception {
        String maritimoes = new Gson().toJson(trMaritimoRepository.findAll());
        return ResponseEntity.ok().body(maritimoes);
    }

    public ResponseEntity findMaritimoByFilter(String jsonString) throws Exception {
        JsonObject jsonFilter = new Gson().fromJson(jsonString, JsonObject.class);
        if (jsonFilter == null || jsonFilter.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        String numeroflota = (jsonFilter.get("numeroflota").isJsonNull()) ? null : jsonFilter.get("numeroflota").getAsString();
        String numeroguia = (jsonFilter.get("numeroguia").isJsonNull()) ? null : jsonFilter.get("numeroguia").getAsString();
        Long idpuerto = (jsonFilter.get("idpuerto").isJsonNull()) ? null : jsonFilter.get("idpuerto").getAsLong();
        Long idtipoProducto = (jsonFilter.get("idtipoProducto").isJsonNull()) ? null : jsonFilter.get("idtipoProducto").getAsLong();
        Long idcliente = (jsonFilter.get("idcliente").isJsonNull()) ? null : jsonFilter.get("idcliente").getAsLong();
        String maritimos = new Gson().toJson(trMaritimoRepository.findAllByFilter(numeroflota, numeroguia, idpuerto, idtipoProducto, idcliente));
        return ResponseEntity.ok().body(maritimos);
    }

    public ResponseEntity saveMaritimo(String jsonString) throws Exception {
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
        if (json.get("numeroFlota") == null || Strings.isEmpty(json.get("numeroFlota").getAsString())) {
            throw new IllegalArgumentException("El numero de flota enviado no se envio o esta vacio");
        }
        if (json.get("numeroFlota").getAsString().length() != 8 || !json.get("numeroFlota").getAsString().matches("^[A-Za-z]{3}[0-9]{4}[A-Za-z]{1}")) {
            throw new ValidationException("El formato del numero de flota no es correcto");
        }
        if (json.get("numeroGuia") == null || Strings.isEmpty(json.get("numeroGuia").getAsString())) {
            throw new IllegalArgumentException("El numero de guia enviado no se envio o esta vacio");
        }
        if (json.get("numeroGuia").getAsString().length() != 10 || !json.get("numeroGuia").getAsString().matches("^[A-Za-z0-9]{10}")) {
            throw new ValidationException("El formato del numero guia no es correcto");
        }

        if (json.get("idpuerto") == null || json.get("idpuerto").isJsonNull()) {
            throw new IllegalArgumentException("El puerto enviado no se envio o esta vacia");
        }
        if (json.get("idtipoProducto") == null || json.get("idtipoProducto").isJsonNull()) {
            throw new IllegalArgumentException("El tipo de producto enviado no se envio o esta vacio");
        }
        if (json.get("idcliente") == null || json.get("idcliente").isJsonNull()) {
            throw new IllegalArgumentException("El cliente enviado no se envio o esta vacio");
        }
        CcPuerto puerto = ccPuertoRepository.findOnePuertoById(json.get("idpuerto").getAsInt());
        if (puerto == null || puerto.getId() == null) {
            throw new NotFoundException("El puerto enviado no existe");
        }
        CcTipoProducto tipoProducto = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("idtipoProducto").getAsInt());
        if (tipoProducto == null || tipoProducto.getId() == null) {
            throw new NotFoundException("El tipo de producto enviado no existe");
        }
        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("idcliente").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente enviado no existe");
        }
        TrMaritimo vmaritimo = gson.fromJson(json, TrMaritimo.class);
        if (trMaritimoRepository.existeNumeroGuiaTrMaritimo(vmaritimo.getNumeroGuia())) {
            throw new ValidationException("El numero guia ya existe");
        }

        Integer cantidad = json.get("cantidadProducto").getAsInt();
        Double precio = json.get("precio").getAsDouble();
        Double descuento = (cantidad > 10) ? 0.05 : 0.00;
        Double precioTotal = (precio - (precio * descuento));
        vmaritimo.setPrecioNormal(precio);
        vmaritimo.setDescuento(descuento);
        vmaritimo.setPrecioEnvio(precioTotal);
        vmaritimo.setPuerto(puerto);
        vmaritimo.setTipoProducto(tipoProducto);
        vmaritimo.setCliente(cliente);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaRegistro = formatter.parse(json.get("fechaRegistro").getAsString());
        vmaritimo.setFechaRegistro(fechaRegistro);
        Date fechaEntrega = formatter.parse(json.get("fechaEntrega").getAsString());
        vmaritimo.setFechaEntrega(fechaEntrega);
        trMaritimoRepository.save(vmaritimo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte maritimo se guardo con exito", OK));
    }

    public ResponseEntity updateMaritimo(String jsonString) throws Exception {
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
        if (json.get("numeroFlota") == null || Strings.isEmpty(json.get("numeroFlota").getAsString())) {
            throw new IllegalArgumentException("El numero de flota enviado no se envio o esta vacio");
        }
        if (json.get("numeroFlota").getAsString().length() != 8 || !json.get("numeroFlota").getAsString().matches("^[A-Za-z]{3}[0-9]{4}[A-Za-z]{1}")) {
            throw new ValidationException("El formato del numero de flota no es correcto");
        }
        if (json.get("numeroGuia") == null || Strings.isEmpty(json.get("numeroGuia").getAsString())) {
            throw new IllegalArgumentException("El numero de guia enviado no se envio o esta vacio");
        }
        if (json.get("numeroGuia").getAsString().length() != 10 || !json.get("numeroGuia").getAsString().matches("^[A-Za-z0-9]{10}")) {
            throw new ValidationException("El formato del numero guia no es correcto");
        }

        if (json.get("idpuerto") == null || json.get("idpuerto").isJsonNull()) {
            throw new IllegalArgumentException("El puerto enviado no se envio o esta vacia");
        }
        if (json.get("idtipoProducto") == null || json.get("idtipoProducto").isJsonNull()) {
            throw new IllegalArgumentException("El tipo de producto enviado no se envio o esta vacio");
        }
        if (json.get("idcliente") == null || json.get("idcliente").isJsonNull()) {
            throw new IllegalArgumentException("El cliente enviado no se envio o esta vacio");
        }
        CcPuerto puerto = ccPuertoRepository.findOnePuertoById(json.get("idpuerto").getAsInt());
        if (puerto == null || puerto.getId() == null) {
            throw new ValidationException("El puerto enviado no existe");
        }
        CcTipoProducto tipoProducto = ccTipoProductoRepository.findOneCcTipoProductoById(json.get("idtipoProducto").getAsInt());
        if (tipoProducto == null || tipoProducto.getId() == null) {
            throw new NotFoundException("El tipo de producto enviado no existe");
        }
        CcCliente cliente = ccClienteRepository.findOneCcClienteById(json.get("idcliente").getAsInt());
        if (cliente == null || cliente.getId() == null) {
            throw new NotFoundException("El cliente enviado no existe");
        }
        TrMaritimo maritimo = trMaritimoRepository.findOneMaritimoById(json.get("id").getAsInt());
        if (maritimo == null || maritimo.getId() == null) {
            throw new NotFoundException("El transporte maritimo ha editar no existe");
        }
        TrMaritimo vmaritimo = gson.fromJson(json, TrMaritimo.class);
        if (trMaritimoRepository.existeNumeroGuiaTrMaritimo(vmaritimo.getNumeroGuia(), vmaritimo.getId())) {
            throw new ValidationException("El numero guia ya existe");
        }
        
        Integer cantidad = json.get("cantidadProducto").getAsInt();
        Double precio = json.get("precio").getAsDouble();
        Double descuento = (cantidad > 10) ? 0.03 : 0.00;
        Double precioTotal = (precio - (precio * descuento));
        maritimo.setPuerto(puerto);
        maritimo.setTipoProducto(tipoProducto);
        maritimo.setCliente(cliente);
        maritimo.setPrecioNormal(precio);
        maritimo.setDescuento(descuento);
        maritimo.setPrecioEnvio(precioTotal);
        maritimo.setCantidadProducto(vmaritimo.getCantidadProducto());
        maritimo.setFechaRegistro(vmaritimo.getFechaRegistro());
        maritimo.setFechaEntrega(vmaritimo.getFechaEntrega());
        maritimo.setNumeroFlota(vmaritimo.getNumeroFlota());
        maritimo.setNumeroGuia(vmaritimo.getNumeroGuia());

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaEntrega = formatter.parse(json.get("fechaEntrega").getAsString());
        maritimo.setFechaEntrega(fechaEntrega);
        trMaritimoRepository.save(maritimo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte maritimo se actualizo con exito", OK));
    }

    public ResponseEntity deleteMaritimo(String jsonString) throws Exception {
        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
        if (json == null || json.entrySet().isEmpty()) {
            throw new NullPointerException("Los datos enviados son incorrectos");
        }
        if (json.get("id") == null || json.get("id").isJsonNull()) {
            throw new IllegalArgumentException("Falta el identificador unico");
        }
        TrMaritimo maritimo = trMaritimoRepository.findOneMaritimoById(json.get("id").getAsInt());
        if (maritimo == null || maritimo.getId() == null) {
            throw new NotFoundException("El transporte maritimo ha eliminar no existe");
        }
        trMaritimoRepository.delete(maritimo);
        return ResponseEntity.ok().body(RestUtils.serverResponse("El transporte maritimo se elimino con exito", OK));
    }
}
