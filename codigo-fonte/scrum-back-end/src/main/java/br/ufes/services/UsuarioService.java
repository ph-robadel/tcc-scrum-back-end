package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.entity.Usuario;
import br.ufes.repository.UsuarioRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario findByNomeUsuario(String nomeUsuario) {
		return usuarioRepository.findByNomeUsuario(nomeUsuario);
	}

	public boolean existsByNomeUsuario(String nomeUsuario) {
		return usuarioRepository.existsByNomeUsuario(nomeUsuario);
	}

	public Usuario getById(Long idUsuario) {
		if (idUsuario == null) {
			return null;
		}

		var usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

		return usuario;
	}

	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public Usuario getUsuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			String nomeUsuario = principal instanceof UserDetails ? ((UserDetails) principal).getUsername()
					: principal.toString();
			return findByNomeUsuario(nomeUsuario);
		}

		return null;
	}

	public ResponseSearch<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO) {
		var listPage = usuarioRepository.search(usuarioFilterDTO);
		var total = usuarioRepository.searchCount(usuarioFilterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public boolean isNomeUsuarioDisponível(String nomeUsuario, Long idUsuario) {
		return usuarioRepository.isNomeUsuarioDisponivel(nomeUsuario, idUsuario);
	}

	public boolean existeUsuariosSalvos() {
		return usuarioRepository.existsBy();
	}
}
