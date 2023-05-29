package com.example.pasarela.Controller.firmaControllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pasarela.Models.Entity.Persona;
import com.example.pasarela.Models.Entity.Titulo;
import com.example.pasarela.Models.Entity.TituloGenerado;
import com.example.pasarela.Models.Entity.Usuario;
import com.example.pasarela.Models.Service.ITituloGeneradoService;
import com.example.pasarela.Models.Service.ITituloService;
import com.example.pasarela.Models.Utils.Archive;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfSignatureAppearance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.List;

@Controller
public class firmaController {
    @Autowired
    private ITituloService tituloService;

    @Autowired
    private ITituloGeneradoService tituloGeneradoService;

    Archive archive = new Archive();

    @RequestMapping(value = "/FirmaR", method = RequestMethod.GET) // Pagina principal
    public String Carrera(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("titulos", tituloService.findAll());

            return "firmar/firmaTitulos";
        } else {
            return "redirect:LoginR";
        }
    }

    @PostMapping("/firmarDocumento")
    public String firmarDocumento(@RequestParam("clavePrivada") String clavePrivada, HttpServletRequest request)
            throws GeneralSecurityException, IOException, DocumentException {
        // Obtener la entidad "Usuario" a partir del usuario en sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        // Obtener la entidad "Persona" asociada al usuario
        Persona persona = usuario.getPersona();

        // Validar la clave privada ingresada
        if (!persona.getClaveP().equals(clavePrivada)) {
            return "FirmaR";
        }

        Path rootPathFirmas = Paths.get("archivos/firmas/");
        Path rootAbsolutPathFirmas = rootPathFirmas.toAbsolutePath();

        Path rootPathTitulos = Paths.get("archivos/titulos/");
        Path rootAbsolutPathTitulos = rootPathTitulos.toAbsolutePath();

        Path rootPathFirmados = Paths.get("archivos/firmados/");
        Path rootAbsolutPathFirmados = rootPathFirmados.toAbsolutePath();

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        List<Titulo> listaTitulos = tituloService.titulosSinFirmar();
        if (listaTitulos != null) {
            for (Titulo titulo : listaTitulos) {
                archive.sign(rootAbsolutPathFirmas.toString() + "/" + persona.getDigital(),
                        persona.getClaveP().toCharArray(), PdfSignatureAppearance.NOT_CERTIFIED,
                        rootAbsolutPathTitulos.toString() + "/" + titulo.getTituloGenerado().getNombre_archivo(),
                        rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                TituloGenerado tituloGenerado = new TituloGenerado();
                // Registrar titulo Generado

                tituloGenerado.setNombre_archivo("/I"+ titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setRuta_archivo(rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                tituloGenerado.setEstado("A");
                TituloGenerado tituloGenerado2 = tituloGeneradoService.registrarTituloGenerado(tituloGenerado);
                titulo.setEstado("F");
                titulo.setTituloGenerado(tituloGenerado2);
                titulo.setDocumento_firmado(
                        rootAbsolutPathFirmados.toString() + "/I" + titulo.getTituloGenerado().getNombre_archivo());
                tituloService.save(titulo);
            }
        }

        return "firmar/firmaTitulos";

    }

}
