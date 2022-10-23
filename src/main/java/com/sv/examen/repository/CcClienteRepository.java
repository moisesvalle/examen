/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcClienteRepository extends JpaRepository<CcCliente, Integer> {
    
    @Query("select c from CcCliente c where c.id = ?1")
    public CcCliente findOneCcClienteById(Integer id);

    @Query("select case when count(c)>0 then true else false end from CcCliente c "
            + "inner join TrMaritimo e on e.cliente = c "
            + "where c.id = ?1")
    public boolean existeCcClienteEnTrMaritimo(Integer id);

    @Query("select case when count(c)>0 then true else false end from CcCliente c "
            + "inner join TrTerrestre e on e.cliente = c "
            + "where c.id = ?1")
    public boolean existeCcClienteEnTrTerrestre(Integer id);
}
