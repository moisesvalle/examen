/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Moises
 */
@Entity
@Table(name = "tr_terrestre")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrTerrestre implements Serializable {

    @Getter
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "cantidad_producto", nullable = false)
    private Integer cantidadProducto;

    @Basic(optional = false)
    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd/mm/yyyy")
    private Date fechaRegistro;

    @Basic(optional = false)
    @Column(name = "fecha_entrega", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd/mm/yyyy")
    private Date fechaEntrega;

    @Basic(optional = false)
    @Column(name = "precio_normal", nullable = false)
    private Double precioNormal;

    @Basic(optional = false)
    @Column(name = "descuento", nullable = false)
    private Double descuento;

    @Basic(optional = false)
    @Column(name = "precio_envio", nullable = false)
    private Double precioEnvio;

    @Basic(optional = false)
    @Column(name = "placa_vehiculo", nullable = false, length = 6)
    private String placaVehiculo;

    @Basic(optional = false)
    @Column(name = "numero_guia", nullable = false, length = 10)
    private String numeroGuia;

    @JoinColumn(name = "id_bodega", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CcBodega bodega;

    @JoinColumn(name = "id_tipo_producto", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CcTipoProducto tipoProducto;

    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CcCliente cliente;

}
