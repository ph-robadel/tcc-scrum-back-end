package br.ufes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.ufes.dto.UsuarioBasicDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
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
	
	public boolean existsByNomeUsuario(String nomeUsuario) throws Exception {
		return usuarioRepository.existsByNomeUsuario(nomeUsuario);
	}

	public Usuario getById(Long idUsuario) throws Exception {
		if (idUsuario == null) {
			return null;
		}

		var usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> 
			new EntityNotFoundException("Usuário não encontrado")
		);

		return usuario;
	}

	public Usuario upsert(Usuario usuario) {
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

	public List<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO) {
		return usuarioRepository.search(usuarioFilterDTO);
	}

	public Long searchCount(UsuarioFilterDTO usuarioFilterDTO) {
		return usuarioRepository.searchCount(usuarioFilterDTO);
	}
}
