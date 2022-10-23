/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.restcontroller;

import com.sv.examen.service.CcPaisService;
import com.sv.examen.utils.RestExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/pais")
public class CcPaisRestController extends RestExceptionHandler {

    @Autowired
    private CcPaisService ccPaisService;

    @GetMapping(value = "/lista", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllPais() throws Exception {
        return ccPaisService.findAllPais();
    }

    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity crearPais(@RequestBody String jsonString) throws Exception {
        return ccPaisService.savePais(jsonString);
    }

    @PutMapping(value = "/editar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editarPais(@RequestBody String jsonString) throws Exception {
        return ccPaisService.updatePais(jsonString);
    }

    @DeleteMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity eliminarPais(@RequestBody String jsonString) throws Exception {
        return ccPaisService.deletePais(jsonString);
    }
}
