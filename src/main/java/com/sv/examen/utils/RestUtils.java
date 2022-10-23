/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.utils;

import com.google.gson.JsonObject;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Moises
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtils {

    public static String serverResponse(String msj, String tipo) {
        JsonObject jsonError = new JsonObject();
        jsonError.addProperty("Estado", tipo);
        jsonError.addProperty("Respuesta", msj);
        return jsonError.toString();
    }

    public static boolean listNullOrEmpty(List<?> lista) {
        return (lista == null || lista.isEmpty());
    }

}
