/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcTipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcTipoProductoRepository extends JpaRepository<CcTipoProducto, Integer> {

    @Query("select t from CcTipoProducto t where t.id = ?1")
    public CcTipoProducto findOneCcTipoProductoById(Integer id);

    @Query("select case when count(t)>0 then true else false end from CcTipoProducto t where t.codigo = ?1")
    public boolean existeCodigoCcTipoProducto(String codigo);

    @Query("select case when count(t)>0 then true else false end from CcTipoProducto t where t.codigo = ?1 and t.id != ?2")
    public boolean existeCodigoCcTipoProducto(String codigo, Integer id);

    @Query("select case when count(t)>0 then true else false end from CcTipoProducto t "
            + "inner join TrMaritimo e on e.tipoProducto = t "
            + "where t.id = ?1")
    public boolean existeCcTipoProductoEnTrMaritimo(Integer id);

    @Query("select case when count(t)>0 then true else false end from CcTipoProducto t "
            + "inner join TrTerrestre e on e.tipoProducto = t "
            + "where t.id = ?1")
    public boolean existeCcTipoProductoEnTrTerrestre(Integer id);
}
