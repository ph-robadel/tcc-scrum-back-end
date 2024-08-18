package br.ufes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.repository.ProjetoUsuarioRepository;

@Service
public class ProjetoUsuarioService {

	@Autowired
	private ProjetoUsuarioRepository projetoUsuarioRepository;

	public ProjetoUsuario save(ProjetoUsuario projetoUsuario) {
		return projetoUsuarioRepository.save(projetoUsuario);
	}

	public ProjetoUsuario getByIdProjetoAndIdUsuario(Long idProjeto, Long idUsuario) {
		if (ObjectUtils.isEmpty(idProjeto) || idProjeto <= 0 || ObjectUtils.isEmpty(idUsuario) || idUsuario <= 0) {
			return null;
		}

		return projetoUsuarioRepository.getByIdProjetoAndIdUsuario(idProjeto, idUsuario);
	}
	
	public List<UsuarioResponseDTO> searchProjetoUsuario(ProjetoUsuarioFilterDTO filterDTO) {
		return projetoUsuarioRepository.search(filterDTO);
	}

	public Long searchProjetoUsuarioCount(ProjetoUsuarioFilterDTO filterDTO) {
		return projetoUsuarioRepository.searchCount(filterDTO);
	}

}