package br.ufes.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;
import util.ResponseSearch;

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

	public ResponseSearch<ItemBacklogSprintDTO> search(Long idSprint, ItemBacklogSprintFilterDTO itemBacklogSprintFilterDTO) throws Exception {
		var itemBacklogSprintMock = itemBacklogSprintService.getMock();
		itemBacklogSprintMock.setItemBacklogProjeto(itemBackLogProjetoService.getBasicMock());
		itemBacklogSprintMock.setResponsavelRealizacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelInclusao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelAprovacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setSprint(sprintService.getBasicMock());
		
		var responseSearch = new ResponseSearch<ItemBacklogSprintDTO>();
		responseSearch.setLista(List.of(itemBacklogSprintMock));
		responseSearch.setTotal(1l);
		return responseSearch;
	}

	public ItemBacklogSprintDTO adicionarItemBacklogSprint(Long idSprint, Long idItemBacklogProjeto, ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) throws Exception {
		var ItemBacklogSprintMock = itemBacklogSprintService.getMock();
		ItemBacklogSprintMock.setItemBacklogProjeto(itemBackLogProjetoService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelRealizacao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelInclusao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setResponsavelAprovacao(usuarioService.getBasicMock());
		ItemBacklogSprintMock.setSprint(sprintService.getBasicMock());
		return ItemBacklogSprintMock;
	}

	public ItemBacklogSprintDTO getById(Long idSprint) throws Exception{
		var itemBacklogSprintMock = itemBacklogSprintService.getMock();
		itemBacklogSprintMock.setItemBacklogProjeto(itemBackLogProjetoService.getBasicMock());
		itemBacklogSprintMock.setResponsavelRealizacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelInclusao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelAprovacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setSprint(sprintService.getBasicMock());
		
		return itemBacklogSprintMock;
	}

	public ItemBacklogSprintDTO atualizar(Long idSprint, SprintUpsertDTO sprintUpsertDTO) throws Exception{
		var itemBacklogSprintMock = itemBacklogSprintService.getMock();
		itemBacklogSprintMock.setItemBacklogProjeto(itemBackLogProjetoService.getBasicMock());
		itemBacklogSprintMock.setResponsavelRealizacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelInclusao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setResponsavelAprovacao(usuarioService.getBasicMock());
		itemBacklogSprintMock.setSprint(sprintService.getBasicMock());
		
		return itemBacklogSprintMock;
	}

	public void delete(Long idSprint) throws Exception{
	}
	
	public void aprovarInclusao(Long idSprint) throws Exception{
	}
	
	public void recusarInclusao(Long idSprint) throws Exception{
	}
	
	public void aprovarRemocao(Long idSprint) throws Exception{
	}
	
	public void recusarRemocao(Long idSprint) throws Exception{
	}

}
