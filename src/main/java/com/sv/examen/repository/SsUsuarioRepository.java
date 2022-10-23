/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.SsUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface SsUsuarioRepository extends JpaRepository<SsUsuario, Integer> {

    @Query("select c from SsUsuario c where c.id = ?1")
    public SsUsuario findOneSsUsuarioById(Integer id);

    @Query("select c from SsUsuario c where c.usuario = ?1")
    public SsUsuario findOneSsUsuarioByUsuario(String usuario);

    @Query("select case when count(c)>0 then true else false end from SsUsuario c where c.usuario = ?1")
    public boolean existeSsUsuario(String usuario);

}
