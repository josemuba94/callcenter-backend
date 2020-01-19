package com.xpertgroup.callcenter.servicios;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.xpertgroup.callcenter.modelo.Calificacion;

public interface CalificarLlamadasService {

	public List<Calificacion> calificar(MultipartFile archivo) throws IOException;
}
