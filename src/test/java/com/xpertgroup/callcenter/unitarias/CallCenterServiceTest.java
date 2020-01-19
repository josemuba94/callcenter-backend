package com.xpertgroup.callcenter.unitarias;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.xpertgroup.callcenter.modelo.Calificacion;
import com.xpertgroup.callcenter.servicios.CallCenterService;

@SpringBootTest
public class CallCenterServiceTest {
	
	@Autowired
	private CallCenterService callCenterService;

	@Test
	public void obtenerCeroEstrellasTest() {
		// Arrange
		int puntaje = -1;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);		
		// Assert
		assertTrue(estrellas == 0);
	}

	@Test
	public void obtenerUnaEstrellaTest() {
		// Arrange
		int puntaje = 24;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);		
		// Assert
		assertTrue(estrellas == 1);
	}

	@Test
	public void obtenerDosEstrellasTest() {
		// Arrange
		int puntaje = 45;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);		
		// Assert
		assertTrue(estrellas == 2);
	}

	@Test
	public void obtenerTresEstrellasTest() {
		// Arrange
		int puntaje = 70;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);		
		// Assert
		assertTrue(estrellas == 3);
	}

	@Test
	public void obtenerCuatroEstrellasTest() {
		// Arrange
		int puntaje = 80;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);		
		// Assert
		assertTrue(estrellas == 4);
	}

	@Test
	public void obtenerCincoEstrellasTest() {
		// Arrange
		int puntaje = 99;		
		// Act
		int estrellas = callCenterService.obtenerEstrellas(puntaje);
		// Assert
		assertTrue(estrellas == 5);
	}
	
	@Test
	public void obtenerPuntajeTest() throws ParseException {
		// Arrange
		String[] mensajes = {"11:55:00 CLIENTE2: Hola",
		                     "11:55:05 ASESOR1: Hola CLIENTE2, bienvenido al centro de servicio.",
		                     "11:55:06 CLIENTE2: Sólo para felicitarlos por el BUEN SERVICIO",
		                     "11:57:16 ASESOR1: Muchas gracias, tomaremos nota de su felicitación.",
		                     "11:57:18 CLIENTE2: Hasta luego.",
		                     "11:58:26 ASESOR1: Hasta luego."};
		// Act
		int puntaje = callCenterService.obtenerPuntaje(mensajes);
		
		// Assert
		assertTrue(puntaje == 45);
	}
	
	@Test
	public void obtenerCalificacionTest() {
		// Arrange
		String conversacion = "3\r\n" + 
				"11:58:30 CLIENTE3: Hola\r\n" + 
				"11:58:35 ASESOR1: Hola CLIENTE3, bienvenido al centro de servicio\r\n" + 
				"11:58:40 CLIENTE3: Buenas tardes, tengo un inconveniente URGENTE y de verdad necesito resolverlo de manera URGENTE\r\n" + 
				"11:58:45 ASESOR1: Con mucho gusto lo atenderemos.\r\n" + 
				"11:58:55 CLIENTE3: Por favor, deben atenderlo de manera URGENTE";
		// Act
		Calificacion calificacion = callCenterService.obtenerCalificacion(conversacion);
		
		assertTrue(calificacion.getConversacion() == 3
					&& calificacion.getPuntaje() == 60
					&& calificacion.getEstrellas() == 3);
	}
}
