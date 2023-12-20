package com.example.pasarela.Models.Entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_revalidacion_generado")
@Getter
@Setter
public class RevalidacionGenerado implements Serializable{
    
     private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_revalidacion_generado;
    private String nombre_archivo;
    private String ruta_archivo;
    private String tipo_archivo;
    private String estado;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "revalidacionGenerado", fetch = FetchType.LAZY)
	private List<Revalidacion> revalidacion;
}
