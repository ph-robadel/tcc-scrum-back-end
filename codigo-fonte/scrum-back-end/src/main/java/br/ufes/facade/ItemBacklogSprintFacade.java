package br.ufes.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;

@Component
public class ItemBacklogSprintFacade {

	@Autowired
	private ItemBacklogProjetoService itemBackLogProjetoService;

	@Autowired
	private ItemBacklogSprintService itemBacklogSprintService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private UsuarioService usuarioService;

	public ItemBacklogSprintDTO search() throws Exception {
		var ItemBacklogSprintMock = itemBacklogSprintService.getMock();
		ItemBacklogSprintMock.setItemBacklogProjeto(itemBackLogProjetoService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelRealizacao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelInclusao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelAprovacao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setSprint(sprintService.getBasicMock());
		return ItemBacklogSprintMock;
	}

}
