package com.xpertgroup.callcenter.servicios;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xpertgroup.callcenter.modelo.Calificacion;

@Service
public class CallCenterService implements CalificarLlamadasService {

	private static final int CERO_ESTRELLAS = 0;
	private static final int UNA_ESTRELLA = 1;
	private static final int DOS_ESTRELLAS = 2;
	private static final int TRES_ESTRELLAS = 3;
	private static final int CUATRO_ESTRELLAS = 4;
	private static final int CINCO_ESTRELLAS = 5;

	private static final int ABANDONADA = -100;
	private static final int URGENTE = -5;
	private static final int BUEN_SERVICIO = 10;
	private static final int EXCELENTE_SERVICIO = 100;
	
	private static final long MILISEGUNDOS_MINUTO = 60000;

	private Logger log = LoggerFactory.getLogger(CallCenterService.class);

	@Override
	public List<Calificacion> calificar(MultipartFile archivoRecibido) throws IOException {		
		String contenido =  new String(archivoRecibido.getBytes());
		
		return Arrays.stream(contenido.split("CONVERSACION "))
				.filter(Strings::isNotBlank)
				.map(conversacion -> obtenerCalificacion(conversacion.trim()))
				.collect(Collectors.toList());
	}
	
	public Calificacion obtenerCalificacion(String conversacion) {
		String[] mensajes = conversacion.split("\n");
		int numeroConversacion = Integer.parseInt(mensajes[0].trim());
		
		if(mensajes.length-1 == 1)
			return new Calificacion(numeroConversacion, ABANDONADA, CERO_ESTRELLAS);
		
		if(conversacion.contains("EXCELENTE SERVICIO"))
			return new Calificacion(numeroConversacion, EXCELENTE_SERVICIO, CINCO_ESTRELLAS);
		
		int puntaje = 0;
		try {
			puntaje = obtenerPuntaje( Arrays.copyOfRange(mensajes, 1, mensajes.length) );
		} catch (ParseException e) {
			log.error("No se pudo calcular el puntaje correspondiente al tiempo de duración de la conversación {}", numeroConversacion);
		}	
		
		return new Calificacion(numeroConversacion, puntaje, obtenerEstrellas(puntaje));
	}
	
	public int obtenerPuntaje(String[] mensajes) throws ParseException {
		// Se toma la igualdad a 5 mensajes como buena (20 puntos)
		int puntaje = (mensajes.length > 5) ? 10 : 20 ;

		for(String mensaje : mensajes) {
			// De la forma como está redactado el enunciado, se entiende que la palabra URGENTE resta 5 puntos
			// en la línea que aparece, sin importar las veces que aparezca dentro de la misma línea
			if(mensaje.contains("URGENTE"))
				puntaje += URGENTE;
			// En el enunciado se menciona una lista de palabras que exclaman buen servicio pero no se adjunta tal lista, por lo tanto,
			// se considera la frase BUEN SERVICIO como único ítem ya que EXCELENTE SERVICIO tiene su propia calificación ya determinada.
			else if(mensaje.contains("BUEN SERVICIO"))
				puntaje += BUEN_SERVICIO;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		long horaInicial = dateFormat.parse(mensajes[0].split(" ", 2)[0]).getTime();
		long horaFinal = dateFormat.parse(mensajes[mensajes.length-1].split(" ", 2)[0]).getTime();		
		puntaje += (horaFinal - horaInicial < MILISEGUNDOS_MINUTO) ? 50 : 25;
		
		return puntaje;
	}
 	
	public int obtenerEstrellas(int puntaje) {
		if(puntaje < 0)
			return CERO_ESTRELLAS;
		if(puntaje < 25)
			return UNA_ESTRELLA;
		if(puntaje < 50)
			return DOS_ESTRELLAS;
		if(puntaje < 75)
			return TRES_ESTRELLAS;
		if(puntaje < 90)
			return CUATRO_ESTRELLAS;
			
		return CINCO_ESTRELLAS;
	}

}
