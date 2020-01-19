package com.xpertgroup.callcenter.servicios;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.xpertgroup.callcenter.modelo.Calificacion;

public interface CalificarLlamadasService {

	public Page<Calificacion> calificar(MultipartFile archivo, int pagina, int tamanoPagina) throws IOException;
}
