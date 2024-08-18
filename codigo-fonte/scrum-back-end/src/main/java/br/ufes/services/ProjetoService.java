package br.ufes.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.entity.Projeto;
import br.ufes.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetoService {

	@Autowired
	private ProjetoRepository projetoRepository;

	public Projeto save(Projeto projeto) {
		return projetoRepository.save(projeto);
	}

	@Deprecated
	public ProjetoDTO getMock() throws Exception {
		var projetoMock = new ProjetoDTO();
		projetoMock.setId(1l);
		projetoMock.setNome("Nome projeto scrum");
		projetoMock.setDescricao("Descrição projeto Scrum");
		projetoMock.setDuracaoHorasDaily(new BigDecimal(0.5));
		projetoMock.setDuracaoHorasPlanning(new BigDecimal(2));
		projetoMock.setDuracaoHorasReview(new BigDecimal(1));
		projetoMock.setDuracaoHorasRetrospective(new BigDecimal(1));
		projetoMock.setDiasSprint(10);

		return projetoMock;
	}

	@Deprecated
	public ProjetoBasicDTO getBasicMock() throws Exception {
		var projetoMock = new ProjetoBasicDTO();
		projetoMock.setId(1l);
		projetoMock.setNome("Nome projeto scrum");

		return projetoMock;
	}

	public List<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO) {
		return projetoRepository.search(filterDTO);
	}

	public Long searchCount(ProjetoFilterDTO filterDTO) {
		return projetoRepository.searchCount(filterDTO);
	}

	public Projeto getById(Long idProjeto) {
		if (idProjeto == null) {
			return null;
		}

		var usuario = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

		return usuario;
	}

}
