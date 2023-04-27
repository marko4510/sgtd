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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "pasarela_documento")
@Getter
@Setter
public class Documento implements Serializable{

    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_documento;
    private String nombre_documento;
    private String estado;
    private String descripcion;

   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "pasarela_tipo_doc_documento",
   joinColumns = @JoinColumn(name = "id_documento"),
   inverseJoinColumns = @JoinColumn(name = "id_tipo_documento"))
   private Set<TipoDocumento> tipoDocumento;
 

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento", fetch = FetchType.LAZY)
	private List<Tramite> tramite;


  @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento", fetch = FetchType.LAZY)
	private List<CostoDocumento> costoDocumento;
}
