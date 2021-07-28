package com.sytecnologias.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sytecnologias.cursomc.domain.Cliente;
import com.sytecnologias.cursomc.repositories.ClienteRepository;
import com.sytecnologias.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		/*
		 * código antigo, antes do tratamento de exceção
		 * 
	 	   Optional<Cliente> obj = repo.findById(id);
		   return obj.orElse(null);
		*/
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}
