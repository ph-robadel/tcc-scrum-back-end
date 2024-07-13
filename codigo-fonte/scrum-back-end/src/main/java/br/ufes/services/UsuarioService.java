package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.ufes.dto.UsuarioBasicDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.entity.Usuario;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario findByNomeUsuario(String nomeUsuario) throws Exception {
		return usuarioRepository.findByNomeUsuario(nomeUsuario);
	}

	public Usuario getById(Long idUsuario) throws Exception {
		if (idUsuario == null) {
			return null;
		}

		var usuarioOptional = usuarioRepository.findById(idUsuario);
		if (usuarioOptional.isEmpty()) {
			throw new EntityNotFoundException("Usuário não encontrado");
		}

		return usuarioOptional.get();
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

	public Usuario getUsuarioAutenticado() throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			String nomeUsuario = principal instanceof UserDetails ? ((UserDetails) principal).getUsername()
					: principal.toString();
			return findByNomeUsuario(nomeUsuario);
		}

		return null;
	}
}
