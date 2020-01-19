package com.xpertgroup.callcenter.unitarias;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpertgroup.callcenter.controlador.CallCenterController;
import com.xpertgroup.callcenter.modelo.Calificacion;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class CallCenterControllerTest {

	private static final String LOCALHOST_API = "http://localhost:8080/api";

	@Autowired
	private CallCenterController callCenterController;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	
	@BeforeAll
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(callCenterController).build();
		objectMapper = new ObjectMapper();
	}
	
	@Test
	public void calificarLlamadasArchivoVacioTest() throws Exception {
		// Arrange
		String conversacion = "";
		MockMultipartFile archivo = new MockMultipartFile("archivo", conversacion, "text/plain", conversacion.getBytes());
		
		// Act
		mockMvc.perform(MockMvcRequestBuilders.multipart(LOCALHOST_API + CallCenterController.URL_CALIFICAR_LLAMADAS)
				.file(archivo))
				// Assert
				.andExpect(status().isBadRequest());
	}

	@Test
	public void calificarLlamadasTest() throws Exception {
		// Arrange
		String conversacion = "CONVERSACION 1\r\n11:51:00 CLIENTE1: Hola";
		MockMultipartFile archivo = new MockMultipartFile("archivo", conversacion, "text/plain", conversacion.getBytes());

		// Act
		String response = mockMvc.perform(MockMvcRequestBuilders.multipart(LOCALHOST_API + CallCenterController.URL_CALIFICAR_LLAMADAS)
				.file(archivo)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		Calificacion calificacion = objectMapper.readValue(response.substring(1, response.length()-1), Calificacion.class);
		
		// Assert
		assertTrue(calificacion.getPuntaje() == -100 && calificacion.getEstrellas() == 0);
	}
	
}
