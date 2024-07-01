package com.example.pasarela.Controller.ApiTitulosPgControllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/titulos")
public class ApiTitulosPgController {

    @Value("${api.key}")
    private String apiKey;

     @Autowired
    private JdbcTemplate jdbcTemplate;

  
    @GetMapping("/diplomado")
    public ResponseEntity<?> getTitulosDiplomado(@RequestHeader("X-Api-Key") String clientApiKey) {
        // Verifica si la clave de API proporcionada coincide con la clave almacenada en tu aplicación
        if (!clientApiKey.equals(apiKey)) {
            return new ResponseEntity<>("Clave de API no válida", HttpStatus.UNAUTHORIZED);
        }

        String sql = "SELECT pt.nro_titulo, pp.nombre, pp.ap_paterno, pp.ap_materno, pt.tipo_titulo, pt.nombre_programa, pt.fecha_generacion, ptg.ruta_archivo " +
                     "FROM pasarela_titulo pt " +
                     "LEFT JOIN pasarela_titulo_generado ptg ON pt.id_titulo_generado = ptg.id_titulo_generado " +
                     "LEFT JOIN pasarela_persona pp ON pt.id_persona = pp.id_persona " +
                     "WHERE pt.tipo_titulo = 'Diplomado' AND pt.estado = 'A'";

        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/especialidadad")
    public ResponseEntity<?> getTitulosEspecialidad(@RequestHeader("X-Api-Key") String clientApiKey) {
        // Verifica si la clave de API proporcionada coincide con la clave almacenada en tu aplicación
        if (!clientApiKey.equals(apiKey)) {
            return new ResponseEntity<>("Clave de API no válida", HttpStatus.UNAUTHORIZED);
        }

        String sql = "SELECT pt.nro_titulo, pp.nombre, pp.ap_paterno, pp.ap_materno, pt.tipo_titulo, pt.fecha_generacion, ptg.ruta_archivo " +
                     "FROM pasarela_titulo pt " +
                     "LEFT JOIN pasarela_titulo_generado ptg ON pt.id_titulo_generado = ptg.id_titulo_generado " +
                     "LEFT JOIN pasarela_persona pp ON pt.id_persona = pp.id_persona " +
                     "WHERE pt.tipo_titulo = 'Especialidad' AND pt.estado = 'A'";

        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

   
    
}
