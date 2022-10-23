/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcCiudad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcCiudadRepository extends JpaRepository<CcCiudad, Integer> {

    @Query("SELECT c FROM CcCiudad c where c.pais.id = ?1")
    public List<CcCiudad> findByPais(Integer id); 

    @Query("select case when count(p)>0 then true else false end from CcCiudad p where p.codigo = ?1")
    public boolean existeCodCiudad(String codigo);

    @Query("select case when count(p)>0 then true else false end from CcCiudad p where p.codigo = ?1 and p.id != ?2")
    public boolean existeCodCiudad(String codigo, Integer id);

    @Query("select c from CcCiudad c where c.id = ?1")
    public CcCiudad findOneCiudadById(Integer id);

    @Query("select case when count(c)>0 then true else false end from CcCiudad c  "
            + "inner join CcBodega e on e.ciudad = p "
            + "where c.id = ?1")
    public boolean existeCcCiudadEnBodega(Integer id);

    @Query("select case when count(c)>0 then true else false end from CcCiudad c  "
            + "inner join CcPuerto e on e.ciudad = c "
            + "where c.id = ?1")
    public boolean existeCcCiudadEnPuerto(Integer id);

    @Modifying
    @Query(value = "delete from CcCiudad where id_pais = ?1", nativeQuery = true)
    public void deleteAllByPais(String idpais);

}
