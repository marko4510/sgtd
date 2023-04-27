package com.example.pasarela.Models.Entity;
import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_nacionalidad")
@Getter
@Setter
public class Nacionalidad implements Serializable{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_nacionalidad;
    private String nombre_nacionalidad;
    private String estado;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nacionalidad", fetch = FetchType.LAZY)
	private List<Departamento> departamento;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nacionalidad", fetch = FetchType.LAZY)
	private List<CostoDocumento> costoDocumento;
}
