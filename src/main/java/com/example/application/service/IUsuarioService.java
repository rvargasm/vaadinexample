package com.example.application.service;

import java.util.List;

import com.example.application.model.Usuario;

public interface IUsuarioService {
	
	public List<Usuario> findAll();
	public long count();
	public void delete(Usuario usuario);
	public void save(Usuario usuario);

}
