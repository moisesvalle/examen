/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.stream.MalformedJsonException;
import java.io.Serializable;
import javassist.NotFoundException;
import javax.xml.bind.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Moises
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler implements Serializable {

    private static final String ERROR = "Error";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handlerException(Exception e) {
        log.info(e.getClass().getSimpleName(), e.getMessage(), e);
        if (e instanceof ValidationException || e.getCause() instanceof ValidationException) {
            return handleValidationException(e.getMessage());
        } else if (e instanceof MalformedJsonException || e.getCause() instanceof MalformedJsonException) {
            return handleMalformedJsonException(e.getMessage());
        } else if (e instanceof NotFoundException || e.getCause() instanceof NotFoundException) {
            return handleNotFoundException(e.getMessage());
        } else if (e instanceof JsonProcessingException || e.getCause() instanceof JsonProcessingException) {
            return handleJsonProcessingException(e.getMessage());
        } else if (e instanceof IllegalArgumentException || e.getCause() instanceof IllegalArgumentException) {
            return handleIllegalArgumentException(e.getMessage());
        } else if (e instanceof UnsupportedOperationException || e.getCause() instanceof UnsupportedOperationException) {
            return handleUnsupportedOperationException(e.getMessage());
        } else if (e instanceof NullPointerException || e.getCause() instanceof NullPointerException) {
            return handleNullPointerException(e.getMessage());
        } else if (e instanceof AuthenticationCredentialsNotFoundException || e.getCause() instanceof AuthenticationCredentialsNotFoundException) {
            return handleAuthenticationCredentialsNotFoundException(e.getMessage());
        } else if (e instanceof AccessDeniedException || e.getCause() instanceof AccessDeniedException) {
            return handleAccessDeniedException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RestUtils.serverResponse(e.getMessage(), ERROR));
    }

    public ResponseEntity<String> handleValidationException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestUtils.serverResponse("Error al validar datos, " + message, ERROR));
    }

    public ResponseEntity<String> handleMalformedJsonException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestUtils.serverResponse("Error al validar la estructura de los datos", ERROR));
    }

    public ResponseEntity<String> handleNotFoundException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestUtils.serverResponse("Error al buscar datos, " + message, ERROR));
    }

    public ResponseEntity<String> handleJsonProcessingException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestUtils.serverResponse("Error al procesar json, " + message, ERROR));
    }

    public ResponseEntity<String> handleIllegalArgumentException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestUtils.serverResponse("Error al validar la peticion, " + message, ERROR));
    }

    public ResponseEntity<String> handleUnsupportedOperationException(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(RestUtils.serverResponse("Error en operacion no compatible, " + message, ERROR));
    }

    public ResponseEntity<String> handleNullPointerException(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(RestUtils.serverResponse("Error en tipo de dato nulo, " + message, ERROR));
    }

    public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestUtils.serverResponse("Acceso no autorizado", ERROR));
    }

    public ResponseEntity<String> handleAccessDeniedException(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestUtils.serverResponse("Acceso no autorizado", ERROR));
    }

}
