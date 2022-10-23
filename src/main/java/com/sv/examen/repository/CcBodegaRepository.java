/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcBodega;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcBodegaRepository extends JpaRepository<CcBodega, Integer> {

    @Query("select b from CcBodega b where b.id = ?1")
    public CcBodega findOneBodegaById(Integer id);

    @Query("SELECT b FROM CcBodega b "
            + " inner join CcPais p on p = b.pais "
            + " inner join CcCiudad c on c = b.ciudad "
            + " where (?1 is null OR b.codigo = ?1) "
            + " and (?2 is null OR b.nombre = ?2) "
            + " and (?3 is null OR p.id = ?3) "
            + " and (?4 is null OR c.id = ?4) ")
    public List<CcBodega> findAllByFilter(String codigo, String nombre, Long idpais, Long idciudad);

    @Query("select case when count(t)>0 then true else false end from CcBodega t where t.codigo = ?1")
    public boolean existeCodigoCcBodega(String codigo);

    @Query("select case when count(t)>0 then true else false end from CcBodega t where t.codigo = ?1 and t.id != ?2")
    public boolean existeCodigoCcBodega(String codigo, Integer id);

    @Query("select case when count(t)>0 then true else false end from CcBodega t "
            + "inner join TrTerrestre e on e.bodega = t "
            + "where t.id = ?1")
    public boolean existeCcBodegaEnTrTerrestre(Integer id);
}
