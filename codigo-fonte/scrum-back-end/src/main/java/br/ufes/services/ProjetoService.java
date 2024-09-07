package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.entity.Projeto;
import br.ufes.repository.ProjetoRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetoService {

	@Autowired
	private ProjetoRepository projetoRepository;

	public Projeto save(Projeto projeto) {
		return projetoRepository.save(projeto);
	}

	public ResponseSearch<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO) {

		var listPage = projetoRepository.search(filterDTO);
		Long total = projetoRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public Projeto getById(Long idProjeto) {
		if (idProjeto == null || idProjeto <= 0) {
			return null;
		}

		return projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));
	}

}
