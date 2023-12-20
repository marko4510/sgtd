package com.example.pasarela.Models.Entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_revalidacion")
@Getter
@Setter
public class Revalidacion implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_revalidacion;
    private String nro_revalidacio;
    
    private LocalDate fecha_generacion;
    
    private String estado;

    //Tabla Archivo Adjunto
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "id_revalidacion_generado")
     private RevalidacionGenerado revalidacionGenerado;
}
