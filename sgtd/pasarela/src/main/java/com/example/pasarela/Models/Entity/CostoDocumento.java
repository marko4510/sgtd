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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_costo_documento")
@Getter
@Setter
public class CostoDocumento implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_costo_documento;
    private String costo;
    private String estado;

    //Tabla Documento
    /*
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_documento")
    private Documento documento;
 */
    //Tabla Tipo Documento
    /* 
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDocumento;
*/
    //Tabla Expedido
    /* 
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nacionalidad")
    private Nacionalidad nacionalidad;
    */

    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "costoDocumento", fetch = FetchType.LAZY)
	//private List<SolicitudLegalizacion> solicitudLegalizacion;

    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "costoDocumento", fetch = FetchType.LAZY)
	//private List<SolicitudSupletorio> solicitudSupletorio;

    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "costoDocumento", fetch = FetchType.LAZY)
	//private List<SolicitudTitulo> solicitudTitulo;
}
