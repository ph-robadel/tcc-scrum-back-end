package br.ufes.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.services.ProjetoService;
import br.ufes.services.SprintService;
import util.ResponseSearch;

@Component
public class SprintFacade {

	@Autowired
	private SprintService sprintService;

	@Autowired
	private ProjetoService projetoService;

	public SprintDTO getById(Long idSprint) throws Exception {
		var mock = sprintService.getMock();
		mock.setProjeto(projetoService.getMock());
		return mock;
	}

	public SprintDTO cadastrarSprint(Long idProjeto, SprintUpsertDTO sprintUpsertDTO) throws Exception {
		var mock = sprintService.getMock();
		mock.setProjeto(projetoService.getMock());
		return mock;
	}

	public ResponseSearch<SprintDTO> search(Long idProjeto, SprintFilterDTO sprintFiltroDTO) throws Exception {
		var mock = sprintService.getMock();
		mock.setProjeto(projetoService.getMock());

		var responseSearch = new ResponseSearch<SprintDTO>();
		responseSearch.setLista(List.of(mock));
		responseSearch.setTotal(1l);
		return responseSearch;
	}

	public SprintDTO atualizarSprint(Long idSprint, SprintUpsertDTO sprintUpsertDTO) throws Exception {
		var mock = sprintService.getMock();
		mock.setProjeto(projetoService.getMock());
		return mock;
	}

	public void deleteSprint(Long idSprint) throws Exception {
	}

}
