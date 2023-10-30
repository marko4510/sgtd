package com.example.pasarela.Models.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pasarela_recibo")
@Getter
@Setter
public class Recibo implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_recibo;
    private String estado_recibo;
    private String estado;
    private String nro_recibo;
    private String tipo_pago_recibo;
    private String nit_recibo;
    private String razon_recibo;
    private String monto_recibo;
    private String cpt_recibo;
    private String qr_recibo;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha_recibo;

    private String archivo_recibo;


    // Tabla Usuario
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
