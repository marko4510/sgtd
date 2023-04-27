package com.example.pasarela.Models.Utils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.springframework.web.multipart.MultipartFile;

import com.example.pasarela.Models.Entity.SolicitudLegalizacion;
import com.example.pasarela.Models.Entity.Tramite;

public class AdjuntarArchivo {
    MultipartFile file; 

    public AdjuntarArchivo() {
     }
    
    public String crearSacDirectorio(String sDirectorio){
        File directorio = new File("C:/"+sDirectorio);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                  return  directorio.getPath()+"/";
            } else {
                  return null;
            }
        }
        
        return directorio.getPath()+"/";
    }

    public Integer adjuntarArchivoTramite(Tramite tramite, String rutaArchivo) throws FileNotFoundException, IOException{

        // Save file on system
    file = tramite.getFile();
    if (!file.getOriginalFilename().isEmpty()) {
       BufferedOutputStream outputStream = new BufferedOutputStream(
             new FileOutputStream(
                   new File(rutaArchivo, tramite.getNombreArchivo())));//file.getOriginalFilename())));
       outputStream.write(file.getBytes());
       outputStream.flush();
       outputStream.close();
    }else{
       return 0; // Error: No es posible adjuntar
    }
    
    return 1; // Adjuntado Correctamente
 }

 public Integer adjuntarArchivoLegalizaciones(SolicitudLegalizacion solicitudLegalizacion, String rutaArchivo) throws FileNotFoundException, IOException{

    // Save file on system
file = solicitudLegalizacion.getFile();
if (!file.getOriginalFilename().isEmpty()) {
   BufferedOutputStream outputStream = new BufferedOutputStream(
         new FileOutputStream(
               new File(rutaArchivo, solicitudLegalizacion.getNombreArchivo())));//file.getOriginalFilename())));
   outputStream.write(file.getBytes());
   outputStream.flush();
   outputStream.close();
}else{
   return 0; // Error: No es posible adjuntar
}

return 1; // Adjuntado Correctamente
} 
}
