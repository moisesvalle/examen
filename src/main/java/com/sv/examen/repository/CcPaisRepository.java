/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcPais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcPaisRepository extends JpaRepository<CcPais, Integer> {

    @Query("select p from CcPais p where p.id = ?1")
    public CcPais findOneCcPaisById(Integer id);

    @Query("select case when count(p)>0 then true else false end from CcPais p where p.codigo = ?1")
    public boolean existeCodCcPais(String codigo);

    @Query("select case when count(p)>0 then true else false end from CcPais p where p.codigo = ?1 and p.id != ?2")
    public boolean existeCodCcPais(String codigo, Integer id);

    @Query("select case when count(p)>0 then true else false end from CcPais p "
            + "inner join CcCiudad e on e.pais = p "
            + "where p.id = ?1")
    public boolean existeCcPaisEnCiudad(Integer id);

    @Query("select case when count(p)>0 then true else false end from CcPais p "
            + "inner join CcBodega e on e.pais = p "
            + "where p.id = ?1")
    public boolean existeCcPaisEnBodega(Integer id);

    @Query("select case when count(p)>0 then true else false end from CcPais p "
            + "inner join CcPuerto e on e.pais = p "
            + "where p.id = ?1")
    public boolean existeCcPaisEnPuerto(Integer id);

}
