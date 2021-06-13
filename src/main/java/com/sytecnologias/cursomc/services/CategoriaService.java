package com.sytecnologias.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sytecnologias.cursomc.domain.Categoria;
import com.sytecnologias.cursomc.repositories.CategoriaRepository;
import com.sytecnologias.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		/*
		 * código antigo, antes do tratamento de exceção
		 * 
	 	   Optional<Categoria> obj = repo.findById(id);
		   return obj.orElse(null);
		*/
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
}
