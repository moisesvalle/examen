/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.restcontroller;

import com.sv.examen.service.TrTerrestreService;
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
@RequestMapping("/terrestre")
public class TrTerrestreRestController extends RestExceptionHandler {

    @Autowired
    private TrTerrestreService trTerrestreService;

    @GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllTerrestre() throws Exception {
        return trTerrestreService.findAllTerrestre();
    }

    @GetMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findTerrestreByFilter(@RequestBody String jsonString) throws Exception {
        return trTerrestreService.findTerrestreByFilter(jsonString);
    }

    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity crearTerrestre(@RequestBody String jsonString) throws Exception {
        return trTerrestreService.saveTerrestre(jsonString);
    }

    @PutMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editarTerrestre(@RequestBody String jsonString) throws Exception {
        return trTerrestreService.updateTerrestre(jsonString);
    }

    @DeleteMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity eliminarTerrestre(@RequestBody String jsonString) throws Exception {
        return trTerrestreService.deleteTerrestre(jsonString);
    }
}
