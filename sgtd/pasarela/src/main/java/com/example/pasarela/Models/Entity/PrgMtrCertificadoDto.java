package com.example.pasarela.Models.Entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrgMtrCertificadoDto  implements Serializable{

private String apellidoPaterno;
  private String apellidoMaterno;
  private String nombrePersona;

  private String nombrePrograma;
  private Integer horasAcademicas;
  private String plan;
  private String version;

  private String codigoMatricula;
  private Integer anhoEjecucion;
  private String correlativo;
  private Integer cantidadModulos;
}
