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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "pasarela_tipo_documento")
@Getter
@Setter
public class TipoDocumento implements Serializable{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_documento;
    private String nombre_tipo_documento;
    private String estado;

    
    @ManyToMany(mappedBy = "tipoDocumento")
    private Set<Documento> documento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
	private List<CostoDocumento> costoDocumento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
	private List<Tramite> tramite;
    
}
