package com.example.pasarela.Models.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
@Table(name = "pasarela_unidad")
@Getter
@Setter
public class Unidad implements Serializable{
    
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_unidad;
    private String nombre_unidad;
    private String estado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidad", fetch = FetchType.LAZY)
	private List<Tramite> tramite;

}
