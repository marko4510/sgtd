package com.example.pasarela.Models.Entity;
import java.io.Serializable;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_solicitud_legalizacion")
@Getter
@Setter
public class SolicitudLegalizacion implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_solicitud_legalizacion;

    private int cantidad_copias;
    private String estado;
    private String tipo_solicitud;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_solicitud;

    @Transient
    private MultipartFile file; 
    
    @Transient
    private String nombreArchivo; 

        //Tabla Archivo Adjunto
       // @ManyToOne(fetch = FetchType.EAGER)
        //@JoinColumn(name = "id_archivo_adjunto")
        //private ArchivoAdjunto archivoAdjunto;

        //Tabla Costo Documento
        //@ManyToOne(fetch = FetchType.EAGER)
        //@JoinColumn(name = "id_costo_documento")
       // private CostoDocumento costoDocumento;

        //Tabla Usuario
       // @ManyToOne(fetch = FetchType.EAGER)
       // @JoinColumn(name = "id_usuario")
        //private Usuario usuario;

}
