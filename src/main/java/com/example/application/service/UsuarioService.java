package com.example.application.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.application.model.Usuario;

@Service
public class UsuarioService implements IUsuarioService {
	
	private List<Usuario> usuarios = new ArrayList<>();
	
	@PostConstruct
	public void precharge() {
		usuarios.add(new Usuario("Raul", "Vargas", "rvargasfact88@gmail.com"));
	}

	@Override
	public List<Usuario> findAll() {
		return usuarios;
	}

	@Override
	public long count() {
		return usuarios.size();
	}

	@Override
	public void delete(Usuario usuario) {
		if(usuarios != null && usuarios.contains(usuario)) {
			usuarios.remove(usuario);
		} else {
			System.out.println("Usuario nulo");
		}
	}

	@Override
	public void save(Usuario usuario) {
		if(usuario == null) {
			System.out.println("Usuario nulo");
			return;
		}
		
		if(usuarios.contains(usuario)) {
			System.out.println("Usuario ya existente");
			return;
		}
		
		usuarios.add(usuario);		
	}
	
	

}
