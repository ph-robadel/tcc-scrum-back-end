package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.ufes.dto.UsuarioBasicDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.entity.Usuario;
import br.ufes.enums.PerfilUsuarioEnum;
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

	public UsuarioBasicDTO getBasicMock() {
		var usuario = new UsuarioBasicDTO();
		usuario.setId(1l);
		usuario.setNomeUsuario("Pedro Mock");
		return usuario;
	}

	public UsuarioResponseDTO getResponseMock() {
		var usuario = new UsuarioResponseDTO();
		usuario.setId(1l);
		usuario.setNomeUsuario("Pedro Mock");
		usuario.setNomeCompleto("Pedro Henrique Mock");
		usuario.setEmail("Email mock");
		usuario.setAtivo(true);
		usuario.setPerfil(PerfilUsuarioEnum.ADMINISTRADOR);
		return usuario;
	}
}
