/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.examen.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "cc_puerto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CcPuerto implements Serializable {

    @Getter
    private static final long serialVersionUID = 1L;
    
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    
    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String codigo;
    
    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Basic(optional = false)
    @Column(name = "numero_muelle", nullable = false, length = 20)
    private String numeroMuelle;
    
    @Basic(optional = false)
    @Column(nullable = false, length = 150)
    private String direccion;
    
    @Basic(optional = false)
    @Column(nullable = false, length = 9)
    private String telefono;
    
    @Basic(optional = false)
    @Column(name = "codigo_postal", nullable = false, length = 3)
    private String codigoPostal;
    
    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    private String email;
    
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CcCiudad ciudad;
    
    @JoinColumn(name = "id_pais", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CcPais pais;
    

  
}
