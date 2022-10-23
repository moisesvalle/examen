/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.repository;

import com.sv.examen.model.TrMaritimo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Moises
 */
@Repository
public interface TrMaritimoRepository extends JpaRepository<TrMaritimo,Integer> {
    
    @Query("select b from TrMaritimo b where b.id = ?1")
    public TrMaritimo findOneMaritimoById(Integer id);

    @Query("SELECT b FROM TrMaritimo b "
            + " inner join CcPuerto p on p = b.puerto "
            + " inner join CcTipoProducto t on t = b.tipoProducto "
            + " inner join CcCliente c on c = b.cliente "
            + " where (?1 is null OR b.numeroFlota = ?1) "
            + " and (?2 is null OR b.numeroGuia = ?2) "
            + " and (?3 is null OR p.id = ?3) "
            + " and (?4 is null OR t.id = ?4) "
            + " and (?5 is null OR c.id = ?5) ")
    public List<TrMaritimo> findAllByFilter(String numeroFlota, String numeroGuia, Long idCcPuerto, Long idTipoProducto, Long idCliente);

    @Query("select case when count(t)>0 then true else false end from TrMaritimo t where t.numeroGuia = ?1")
    public boolean existeNumeroGuiaTrMaritimo(String codigo);

    @Query("select case when count(t)>0 then true else false end from TrMaritimo t where t.numeroGuia = ?1 and t.id != ?2")
    public boolean existeNumeroGuiaTrMaritimo(String codigo, Integer id);
}
