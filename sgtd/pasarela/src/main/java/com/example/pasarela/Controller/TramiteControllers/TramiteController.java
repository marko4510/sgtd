package com.example.pasarela.Controller.TramiteControllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.pasarela.Models.Entity.ArchivoAdjunto;
import com.example.pasarela.Models.Entity.Carrera;
import com.example.pasarela.Models.Entity.Documento;
import com.example.pasarela.Models.Entity.TipoDocumento;
import com.example.pasarela.Models.Service.IArchivoAdjuntoService;
import com.example.pasarela.Models.Service.ICarreraService;
import com.example.pasarela.Models.Service.IDocumentoService;
import com.example.pasarela.Models.Service.IPersonaService;
import com.example.pasarela.Models.Service.ITipoDocumentoService;
import com.example.pasarela.Models.Service.ITramiteService;
import com.example.pasarela.Models.Service.IUnidadService;
import com.example.pasarela.Models.Utils.AdjuntarArchivo;
import com.example.pasarela.Models.Entity.Tramite;
import com.example.pasarela.Models.Entity.Unidad;

@Controller
public class TramiteController {
    @Autowired
    private ITramiteService tramiteService;

    @Autowired
    private IArchivoAdjuntoService archivoAdjuntoService;

    @Autowired
    private IDocumentoService documentoService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private ITipoDocumentoService tipoDocumentoService;

    @Autowired
    private IUnidadService unidadService;

    @Autowired
    private ICarreraService carreraService;

    // Formulario para Registrar Tramite
    @RequestMapping(value = "/TramiteR", method = RequestMethod.GET) // Pagina principal
    public String Tramite(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("documentos", documentoService.findAll());
            model.addAttribute("tramite", new Tramite());
            model.addAttribute("tramites", tramiteService.findAll());
            model.addAttribute("personas", personaService.findAll());
            model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
            model.addAttribute("unidades", unidadService.findAll());
            return "tramite/registrarTramites";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/TramiteF", method = RequestMethod.POST) // Enviar datos de Registro a Lista
    public String TramiteF(@Validated Tramite tramite, Model model) throws FileNotFoundException, IOException { // validar
                                                                                                                // los

        MultipartFile multipartFile = tramite.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();

        System.out.println(tramite.getTipoDocumento().getNombre_tipo_documento());
        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("LEGALIZACIÓN")) {
        String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/legalizacion");
        model.addAttribute("di", rutaArchivo);
        List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
        tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        archivoAdjunto.setNombre_archivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
        archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
        archivoAdjunto.setRuta_archivo("SGTD/tramite/legalizacion/");
        archivoAdjunto.setEstado("A");
        ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
        List<Tramite> listaT = tramiteService.findAll();
        tramite.setNro_tramite("" + (listaT.size() + 1));
        tramite.setArchivoAdjunto(archivoAdjunto2);
        tramite.setEstado("A");

        tramiteService.save(tramite);
        }

        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("SUPLETORIO") ) {
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/supletorio");
            model.addAttribute("di", rutaArchivo);
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
            Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
            archivoAdjunto.setNombre_archivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
            archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
            archivoAdjunto.setRuta_archivo("SGTD/tramite/supletorio/");
            archivoAdjunto.setEstado("A");
            ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
            List<Tramite> listaT = tramiteService.findAll();
            tramite.setNro_tramite("" + (listaT.size() + 1));
            tramite.setArchivoAdjunto(archivoAdjunto2);
            tramite.setEstado("A");
    
            tramiteService.save(tramite);
            }
            
            if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("TÍTULO / DIPLOMA")) {
                String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/titulos");
                model.addAttribute("di", rutaArchivo);
                List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
                tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
                Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
                archivoAdjunto.setNombre_archivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
                archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
                archivoAdjunto.setRuta_archivo("SGTD/tramite/titulos/");
                archivoAdjunto.setEstado("A");
                ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
                List<Tramite> listaT = tramiteService.findAll();
                tramite.setNro_tramite("" + (listaT.size() + 1));
                tramite.setArchivoAdjunto(archivoAdjunto2);
                tramite.setEstado("A");
        
                tramiteService.save(tramite);
                }   

                if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("PROVISIÓN NACIONAL")) {
                    String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/provisionN");
                    model.addAttribute("di", rutaArchivo);
                    List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
                    tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
                    Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
                    archivoAdjunto.setNombre_archivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +".pdf");
                    archivoAdjunto.setTipo_archivo(multipartFile.getContentType());
                    archivoAdjunto.setRuta_archivo("SGTD/tramite/provisionN/");
                    archivoAdjunto.setEstado("A");
                    ArchivoAdjunto archivoAdjunto2 = archivoAdjuntoService.registrarArchivoAdjunto(archivoAdjunto);
                    List<Tramite> listaT = tramiteService.findAll();
                    tramite.setNro_tramite("" + (listaT.size() + 1));
                    tramite.setArchivoAdjunto(archivoAdjunto2);
                    tramite.setEstado("A");
            
                    tramiteService.save(tramite);
                    } 
      

        return "redirect:/TramiteL";
    }

    // Listar Tramites
    @RequestMapping(value = "/TramiteL", method = RequestMethod.GET) // Pagina principal
    public String facultadL(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            

            

            model.addAttribute("tramites", tramiteService.findAll());
            model.addAttribute("listaLegalizacion", tramiteService.listaCarpetaLegalizacion());
           
            model.addAttribute("listaSupletorio", tramiteService.listaCarpetaSupletorio());
            model.addAttribute("listaTitulos", tramiteService.listaCarpetaTitulos());
            model.addAttribute("listaProvision", tramiteService.listaCarpetaProvision());
            model.addAttribute("unidades", unidadService.findAll());
        model.addAttribute("carreras", carreraService.findAll());
            return "tramite/listarTramites";
        } else {
            return "redirect:LoginR";
        }
    }

    @RequestMapping(value = "/editar-tramite/{id_tramite}")
    public String editar_p(@PathVariable("id_tramite") Long id_tramite, Model model) {

        Tramite tramite = tramiteService.findOne(id_tramite);

        model.addAttribute("tramite", tramite);
        model.addAttribute("documentos", documentoService.findAll());
        model.addAttribute("tramites", tramiteService.findAll());
        model.addAttribute("personas", personaService.findAll());
        model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
        model.addAttribute("unidades", unidadService.findAll());
        model.addAttribute("edit", "true");
        return "tramite/registrarTramites";

    }

    @PostMapping(value = "/TramiteModF")
    public String modificarContratacionesSicoes(@Validated Tramite tramite, Model model, HttpServletRequest request)
            throws IOException {

        MultipartFile multipartFile = tramite.getFile();
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        AdjuntarArchivo adjuntarArchivo = new AdjuntarArchivo();

        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("LEGALIZACIÓN")) {
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/legalizacion");
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +"-mod"+".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorTramite(tramite.getArchivoAdjunto().getId_archivo_adjunto());
            barchivoAdjunto.setNombre_archivo(tramite.getNombreArchivo());
            barchivoAdjunto.setRuta_archivo("SGTD/tramite/legalizacion/");
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        tramite.setNro_tramite(tramite.getNro_tramite());
        tramite.setEstado("A");
        tramiteService.save(tramite);

        }

        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("SUPLETORIO")) {
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/supletorio");
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +"-mod"+".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorTramite(tramite.getArchivoAdjunto().getId_archivo_adjunto());
            barchivoAdjunto.setNombre_archivo(tramite.getNombreArchivo());
            barchivoAdjunto.setRuta_archivo("SGTD/tramite/supletorio/");
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        tramite.setNro_tramite(tramite.getNro_tramite());
        tramite.setEstado("A");
        tramiteService.save(tramite);

        }
      
        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("TÍTULO / DIPLOMA")) {
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/titulos");
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +"-mod"+".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorTramite(tramite.getArchivoAdjunto().getId_archivo_adjunto());
            barchivoAdjunto.setNombre_archivo(tramite.getNombreArchivo());
            barchivoAdjunto.setRuta_archivo("SGTD/tramite/titulos/");
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        tramite.setNro_tramite(tramite.getNro_tramite());
        tramite.setEstado("A");
        tramiteService.save(tramite);

        }

        if (tramite.getTipoDocumento().getNombre_tipo_documento().equals("PROVISIÓN NACIONAL")) {
            String rutaArchivo = adjuntarArchivo.crearSacDirectorio("SGTD/tramite/provisionN");
            List<ArchivoAdjunto> listArchivos = archivoAdjuntoService.listarArchivoAdjunto();
            tramite.setNombreArchivo((listArchivos.size() + 1)+"-" +tramite.getPersona().getCi()  +"-mod"+".pdf");
        Integer ad = adjuntarArchivo.adjuntarArchivoTramite(tramite, rutaArchivo);
        if (ad == 1) {
            ArchivoAdjunto barchivoAdjunto = archivoAdjuntoService
                    .buscarArchivoAdjuntoPorTramite(tramite.getArchivoAdjunto().getId_archivo_adjunto());
            barchivoAdjunto.setNombre_archivo(tramite.getNombreArchivo());
            barchivoAdjunto.setRuta_archivo("SGTD/tramite/provisionN/");
            archivoAdjuntoService.modificarArchivoAdjunto(barchivoAdjunto);
        }
        tramite.setNro_tramite(tramite.getNro_tramite());
        tramite.setEstado("A");
        tramiteService.save(tramite);

        }

        

        return "redirect:/TramiteL";

    }

    @RequestMapping(value = "/openFileTramite/{id}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody FileSystemResource abrirArchivoMedianteResourse(HttpServletResponse response,
            @PathVariable("id") long id_tramite) throws FileNotFoundException {
        ArchivoAdjunto ArchivoAdjuntos = archivoAdjuntoService.buscarArchivoAdjuntoPorTramite(id_tramite);
        System.out.println(ArchivoAdjuntos + "+++++++++++++++++++++++++++++++++");
        File file = new File("C:/" + ArchivoAdjuntos.getRuta_archivo() + ArchivoAdjuntos.getNombre_archivo());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }


    @GetMapping("/inicioReportes")
    public String inicioReporteCarpetas(Model model) {
        model.addAttribute("tipoDocumentos", tipoDocumentoService.findAll());
        model.addAttribute("documentos", documentoService.findAll());
        model.addAttribute("unidades", unidadService.findAll());
        model.addAttribute("carreras", carreraService.findAll());
        return "tramite/reporte/generarReportes.html";
    }
    /*aqui debe venir  para el select anidado */
    @RequestMapping(value = "/documentos", method = RequestMethod.GET)
    public @ResponseBody List<Documento> findAllAgencie(
            @RequestParam(value = "tipoDocId", required = true) Long TdocId) {
        List<Documento> Documentos = documentoService.documentoPorIdTipodoc(TdocId);
        return Documentos;
    }

    @PostMapping("/generarReporteUnidadTipoDocFechas")
    public String generarReporteUnidadTipoDocFechas(
        @RequestParam("id_tipo_documento") Long id_tipo_documento,
        @RequestParam("id_unidad") Long id_unidad,
        @RequestParam("id_documento") Long id_documento,
        @RequestParam("fechaInicio") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaFin,
            Model model) throws FileNotFoundException, IOException {

                Unidad unidad = unidadService.findOne(id_unidad);
                TipoDocumento tipoDocumento = tipoDocumentoService.findOne(id_tipo_documento);
                Documento documento = documentoService.findOne(id_documento);
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateado = formato.format(fechaInicio);
                String fechaFinFormateado = formato.format(fechaFin);
        
        model.addAttribute("tramites", tramiteService.tramitePorUnidadTipoDocumentoDocumentoFechas(id_unidad, id_tipo_documento, id_documento, fechaInicio, fechaFin));
        model.addAttribute("unidad", unidad);
        model.addAttribute("tipoDocumento", tipoDocumento);
        model.addAttribute("documento", documento);
        model.addAttribute("fechaInicio", fechaInicioFormateado);
        model.addAttribute("fechaFin", fechaFinFormateado);

        return "tramite/reporte/reporteTramiteTipoDocumentosFechas.html";
    }

    @PostMapping("/generarReporteUnidadTipoDocDocGestion")
    public String generarReporteUnidadTipoDocDocGestion(
        @RequestParam("id_tipo_documento") Long id_tipo_documento,
        @RequestParam("id_documento") Long id_documento,
        @RequestParam("id_unidad") Long id_unidad,
        @RequestParam("gestion") String gestion,
            Model model) throws FileNotFoundException, IOException {

                Unidad unidad = unidadService.findOne(id_unidad);
                TipoDocumento tipoDocumento = tipoDocumentoService.findOne(id_tipo_documento);
                Documento documento = documentoService.findOne(id_documento);
        
        model.addAttribute("tramites", tramiteService.tramitePorUnidadTipoDocumentoDocumentoGestion(id_unidad, id_tipo_documento, id_documento, gestion));
        model.addAttribute("unidad", unidad);
        model.addAttribute("gestion", gestion);
        model.addAttribute("tipoDocumento", tipoDocumento);
        model.addAttribute("documento", documento);

        return "tramite/reporte/reporteTramiteTipoDocumentosDoc.html";
    }

    @PostMapping("/generarReporteCarpetaDocumento")
    public String generarReporteCarpetaDocumento(@RequestParam("id_documento") Long id_documento,
            Model model) throws FileNotFoundException, IOException {

        
        
        model.addAttribute("tramites", tramiteService.tramitePorDocumento(id_documento));
      

        return "tramite/reporte/reporteDocumento.html";
    }

    @PostMapping("/generarReporteCarpetaDocumentoTipoDocumento")
    public String generarReporteCarpetaDocumentoTipoDocumento(@RequestParam("id_documento") Long id_documento,
    @RequestParam("id_tipo_documento") Long id_tipo_documento,
            Model model) throws FileNotFoundException, IOException {

        
        
        model.addAttribute("tramites", tramiteService.tramitePorDocumentoTipoDocumento(id_documento, id_tipo_documento));
      

        return "tramite/reporte/reporteDocumentoTipoDocumento.html";
    }
    //REPORTES POR GESTION
    @PostMapping("/generarReporteTituladosUnidadGestion")
    public String generarReporteTituladosUnidadGestion(
        @RequestParam("id_unidad") Long id_unidad,
        @RequestParam("gestion") String gestion,
            Model model) throws FileNotFoundException, IOException {

        Unidad unidad = unidadService.findOne(id_unidad);
    
        
        model.addAttribute("tramites", tramiteService.reporteTituladosPorUnidadGestion(id_unidad, gestion));
        model.addAttribute("unidad", unidad);
        model.addAttribute("gestion", gestion);

        return "tramite/reporte/reporteUnidad.html";
    }


    @PostMapping("/generarReporteCarpetaUnidadCarreraGestion")
    public String generarReporteCarpetaUnidadCarreraGestion(
        @RequestParam("id_unidad") Long id_unidad,
        @RequestParam("id_carrera") Long id_carrera,
        @RequestParam("gestion") String gestion,
            Model model) throws FileNotFoundException, IOException {

        Unidad unidad = unidadService.findOne(id_unidad);
        Carrera carrera = carreraService.findOne(id_carrera);
        
        model.addAttribute("tramites", tramiteService.reporteCarpetaPorUnidadCarreraGestion(id_unidad, id_carrera, gestion));
        model.addAttribute("unidad", unidad);
        model.addAttribute("carrera", carrera);
        model.addAttribute("gestion", gestion);

        return "tramite/reporte/reporteGestion.html";
    }

    @PostMapping("/generarReporteCarpetaUnidadCarreraGestionSexo")
    public String generarReporteCarpetaUnidadCarreraGestionSexo(
        @RequestParam("id_unidad") Long id_unidad,
        @RequestParam("id_carrera") Long id_carrera,
        @RequestParam("gestion") String gestion,
        @RequestParam("sexo") String sexo,
            Model model) throws FileNotFoundException, IOException {

        Unidad unidad = unidadService.findOne(id_unidad);
        Carrera carrera = carreraService.findOne(id_carrera);
        
        model.addAttribute("tramites", tramiteService.reporteCarpetaPorUnidadCarreraGestionSexo(id_unidad, id_carrera, gestion, sexo));
        model.addAttribute("unidad", unidad);
        model.addAttribute("carrera", carrera);
        model.addAttribute("gestion", gestion);
        model.addAttribute("sexo", sexo);

        return "tramite/reporte/reporteGestionSexo.html";
    }

    @PostMapping("/generarReporteTituladosPorFechas")
    public String generarReporteTituladosPorFechas(
        @RequestParam("fechaInicio") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaFin,
            Model model) throws FileNotFoundException, IOException {

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateado = formato.format(fechaInicio);
                String fechaFinFormateado = formato.format(fechaFin);
        model.addAttribute("tramites", tramiteService.reporteTituladosPorFechas(fechaInicio, fechaFin));
        model.addAttribute("fechaInicio", fechaInicioFormateado);
        model.addAttribute("fechaFin", fechaFinFormateado);
        return "tramite/reporte/reporteFechas.html";
    }

    @PostMapping("/generarReporteTituladosDoctorado")
    public String generarReporteTituladosDoctorado(
        @RequestParam("gestion") String gestion,
       
            Model model) throws FileNotFoundException, IOException {

            
        model.addAttribute("tramites", tramiteService.reporteTituladosDoctorado(gestion));
        model.addAttribute("gestion", gestion);
        return "tramite/reporte/reporteDoctorado.html";
    }

    @PostMapping("/generarReporteTituladosMaestria")
    public String generarReporteTituladosMaestria(
        @RequestParam("gestion") String gestion,
       
            Model model) throws FileNotFoundException, IOException {

            
        model.addAttribute("tramites", tramiteService.reporteTituladosMaestria(gestion));
        model.addAttribute("gestion", gestion);
        return "tramite/reporte/reporteMaestria.html";
    }

    @PostMapping("/generarReporteTituladosPosgradoPorFechas")
    public String generarReporteTituladosPosgradoPorFechas(
        @RequestParam("fechaInicio") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(pattern= "yyyy-MM-dd") Date fechaFin,
            Model model) throws FileNotFoundException, IOException {

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                String fechaInicioFormateado = formato.format(fechaInicio);
                String fechaFinFormateado = formato.format(fechaFin);
        model.addAttribute("tramites", tramiteService.reporteTituladosPosgradoPorFechas(fechaInicio, fechaFin));
        model.addAttribute("fechaInicio", fechaInicioFormateado);
        model.addAttribute("fechaFin", fechaFinFormateado);
        return "tramite/reporte/reportePosgradoFechas.html";
    }


}
