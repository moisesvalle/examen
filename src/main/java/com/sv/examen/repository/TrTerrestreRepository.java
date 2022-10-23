/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.TrTerrestre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface TrTerrestreRepository extends JpaRepository<TrTerrestre, Integer> {

    @Query("select b from TrTerrestre b where b.id = ?1")
    public TrTerrestre findOneTerrestreById(Integer id);

    @Query("SELECT b FROM TrTerrestre b "
            + " inner join CcBodega p on p = b.bodega "
            + " inner join CcTipoProducto t on t = b.tipoProducto "
            + " inner join CcCliente c on c = b.cliente "
            + " where (?1 is null OR b.placaVehiculo = ?1) "
            + " and (?2 is null OR b.numeroGuia = ?2) "
            + " and (?3 is null OR p.id = ?3) "
            + " and (?4 is null OR t.id = ?4) "
            + " and (?5 is null OR c.id = ?5) ")
    public List<TrTerrestre> findAllByFilter(String placaVehiculo, String numeroGuia, Long idBodega, Long idTipoProducto, Long idCliente);

    @Query("select case when count(t)>0 then true else false end from TrTerrestre t where t.numeroGuia = ?1")
    public boolean existeNumeroGuiaTrTerrestre(String codigo);

    @Query("select case when count(t)>0 then true else false end from TrTerrestre t where t.numeroGuia = ?1 and t.id != ?2")
    public boolean existeNumeroGuiaTrTerrestre(String codigo, Integer id);

}
