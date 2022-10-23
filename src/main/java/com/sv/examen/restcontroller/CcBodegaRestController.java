/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.restcontroller;

import com.sv.examen.service.CcBodegaService;
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
@RequestMapping("/bodega")
public class CcBodegaRestController extends RestExceptionHandler {

    @Autowired
    private CcBodegaService ccBodegaService;

    @GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllBodega() throws Exception {
        return ccBodegaService.findAllBodega();
    }

    @GetMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findBodegaByFilter(@RequestBody String jsonString) throws Exception {
        return ccBodegaService.findBodegaByFilter(jsonString);
    }

    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity crearBodega(@RequestBody String jsonString) throws Exception {
        return ccBodegaService.saveBodega(jsonString);
    }

    @PutMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editarBodega(@RequestBody String jsonString) throws Exception {
        return ccBodegaService.updateBodega(jsonString);
    }

    @DeleteMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity eliminarBodega(@RequestBody String jsonString) throws Exception {
        return ccBodegaService.deleteBodega(jsonString);
    }
}
