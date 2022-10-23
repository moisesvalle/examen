/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.CcPuerto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface CcPuertoRepository extends JpaRepository<CcPuerto, Integer> {

    @Query("select p from CcPuerto p where p.id = ?1")
    public CcPuerto findOnePuertoById(Integer id);

    @Query("SELECT b FROM CcPuerto b "
            + " inner join CcPais p on p = b.pais "
            + " inner join CcCiudad c on c = b.ciudad "
            + " where (?1 is null OR b.codigo = ?1) "
            + " and (?2 is null OR b.nombre = ?2) "
            + " and (?3 is null OR b.numeroMuelle = ?3) "
            + " and (?4 is null OR p.id = ?4) "
            + " and (?5 is null OR c.id = ?5) ")
    public List<CcPuerto> findAllByFilter(String codigo, String nombre, String numeroMuelle, Long idpais, Long idciudad);

    @Query("select case when count(t)>0 then true else false end from CcPuerto t where t.codigo = ?1")
    public boolean existeCodigoCcPuerto(String codigo);

    @Query("select case when count(t)>0 then true else false end from CcPuerto t where t.codigo = ?1 and t.id != ?2")
    public boolean existeCodigoCcPuerto(String codigo, Integer id);

    @Query("select case when count(t)>0 then true else false end from CcPuerto t "
            + "inner join TrMaritimo e on e.puerto = t "
            + "where t.id = ?1")
    public boolean existeCcPuertoEnTrMaritimo(Integer id);
}
