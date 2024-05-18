package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.ufes.entity.Usuario;
import br.ufes.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public UserDetails findByNomeUsuario(String nomeUsuario) throws Exception {
		return usuarioRepository.findByNomeUsuario(nomeUsuario);
	}

	public Usuario insert(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
}
