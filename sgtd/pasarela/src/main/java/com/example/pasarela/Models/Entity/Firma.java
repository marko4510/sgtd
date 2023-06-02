package com.example.pasarela.Models.Entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_firma")
@Getter
@Setter
public class Firma implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_firma;

     // Tabla Autoridad
     @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_autoridad")
     private Autoridad autoridad;


      // Tabla Titulo
      @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id_titulo")
      private Titulo titulo;
}
