package br.ufes.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.SprintDTO;
import br.ufes.services.ProjetoService;
import br.ufes.services.SprintService;

@Component
public class SprintFacade {

	@Autowired
	private SprintService sprintService;

	@Autowired
	private ProjetoService projetoService;

	public SprintDTO getAllByProjeto() throws Exception {
		var mock = sprintService.getMock();
		mock.setProjeto(projetoService.getMock());
		return mock;
	}

}
