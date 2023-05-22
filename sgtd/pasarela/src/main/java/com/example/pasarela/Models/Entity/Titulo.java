package com.example.pasarela.Models.Entity;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "pasarela_titulo")
@Getter
@Setter
public class Titulo implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_titulo;
    private String nro_titulo;
    private String observacion;
    
    
    private LocalDate fecha_generacion;
    
    private String estado;

 

     //Tabla Archivo Adjunto
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "id_titulo_generado")
     private TituloGenerado tituloGenerado;



}