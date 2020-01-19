package com.xpertgroup.callcenter.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xpertgroup.callcenter.servicios.CalificarLlamadasService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200" })
public class CallCenterController {

	public static final String URL_CALIFICAR_LLAMADAS = "/calificar/{pagina}";
	
	private static final int TAMANO_PAGINA = 2;
	
	@Autowired
	private CalificarLlamadasService calificarLlamadasService;
	
	@PostMapping(URL_CALIFICAR_LLAMADAS)
	public ResponseEntity<Object> calificarLlamadas(@RequestParam("archivo") MultipartFile archivo, @PathVariable int pagina) {
		Map<String, Object> response = new HashMap<>();
		
		if(archivo.isEmpty()) {
			response.put("error", "El archivo enviado está vacío.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {			
			return ResponseEntity.ok( calificarLlamadasService.calificar(archivo, pagina, TAMANO_PAGINA) );
			
		} catch (IOException exception) {
			response.put("error", exception.getMessage().concat(": ").concat(exception.getCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
}
