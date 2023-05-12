package com.example.pasarela.Models.Entity;
import java.io.Serializable;
import java.sql.Date;
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
import javax.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "pasarela_tramite")
@Getter
@Setter
public class Tramite {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tramite;
    private String nro_tramite;
    private String observacion;
    private Date fecha_recepcion;
    private Date fecha_titulacion;
    private String estado;
    private String gestion;
   
     
    @Transient
    private MultipartFile file; 
    
    @Transient
    private String nombreArchivo; 

       //Tabla Archivo Adjunto
       @ManyToOne(fetch = FetchType.EAGER)
       @JoinColumn(name = "id_archivo_adjunto")
       private ArchivoAdjunto archivoAdjunto;

       //Tabla Documento
       @ManyToOne(fetch = FetchType.EAGER)
       @JoinColumn(name = "id_documento")
       private Documento documento;

       
       //Tabla Persona
       @ManyToOne(fetch = FetchType.EAGER)
       @JoinColumn(name = "id_persona")
       private Persona persona;

        //Tabla Persona
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_tipo_documento")
        private TipoDocumento tipoDocumento;

         //Tabla Unidad
         @ManyToOne(fetch = FetchType.EAGER)
         @JoinColumn(name = "id_unidad")
         private Unidad unidad;
}
