/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.restcontroller;

import com.sv.examen.service.CcPuertoService;
import com.sv.examen.utils.RestExceptionHandler;
import com.sv.examen.utils.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Moises
 */
@RestController
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/puerto")
public class CcPuertoRestController extends RestExceptionHandler {

    @Autowired
    private CcPuertoService ccPuertoService;

    @GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllPuerto() throws Exception {
        return ccPuertoService.findAllPuerto();
    }

    @GetMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findPuertoByFilter(@RequestBody String jsonString) throws Exception {
        return ccPuertoService.findPuertoByFilter(jsonString);
    }

    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity crearPuerto(@RequestBody String jsonString) throws Exception {
        return ccPuertoService.savePuerto(jsonString);
    }

    @PutMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editarPuerto(@RequestBody String jsonString) throws Exception {
        return ccPuertoService.updatePuerto(jsonString);
    }

    @DeleteMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity eliminarPuerto(@RequestBody String jsonString) throws Exception {
        return ccPuertoService.deletePuerto(jsonString);
    }
}
