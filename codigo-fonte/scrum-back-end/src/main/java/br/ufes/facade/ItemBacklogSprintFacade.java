package br.ufes.facade;

import java.time.LocalDateTime;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.enums.SituacaoItemSprintEnum;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ItemBacklogSprintValidate;
import br.ufes.validate.ProjetoUsuarioValidate;
import br.ufes.validate.ProjetoValidate;
import br.ufes.validate.SprintValidate;

@Component
public class ItemBacklogSprintFacade {

	@Autowired
	private ItemBacklogProjetoService itemBacklogProjetoService;

	@Autowired
	private ItemBacklogSprintService itemBacklogSprintService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProjetoUsuarioValidate projetoUsuarioValidate;

	@Autowired
	private ProjetoValidate projetoValidate;

	@Autowired
	private ItemBacklogSprintValidate itemBacklogSprintValidate;
	
	@Autowired
	private SprintValidate sprintValidate;

	@Autowired
	private ModelMapper modelMapper;

	public ResponseSearch<ItemBacklogSprintBasicDTO> search(Long idSprint, ItemBacklogSprintFilterDTO filterDTO)
			throws Exception {

		var sprint = sprintService.getById(idSprint);
		var projeto = sprint.getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		filterDTO.setIdSprint(idSprint);

		return itemBacklogSprintService.search(filterDTO);
	}

	public ItemBacklogSprintDTO adicionarItemBacklogSprint(Long idSprint, Long idItemBacklogProjeto,
			ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) throws Exception {

		var sprint = sprintService.getById(idSprint);
		var itemBacklogProjeto = itemBacklogProjetoService.getById(idSprint);
		var projeto = sprint.getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		itemBacklogSprintValidate.validateSave(itemBacklogSprintUpsertDTO, sprint);
		var itemBacklogSprint = modelMapper.map(itemBacklogSprintUpsertDTO, ItemBacklogSprint.class);

		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		var prioridadeNovoItem = itemBacklogSprintService.obterNumeroPrioridadeNovoItem(idSprint);
		var responsavelRealizacao = usuarioService.getById(itemBacklogSprintUpsertDTO.getIdResponsavelRealizacao());
		var dataHoraAtual = LocalDateTime.now();

		itemBacklogSprint.setPrioridade(prioridadeNovoItem);
		itemBacklogSprint.setResponsavelRealizacao(responsavelRealizacao);
		itemBacklogSprint.setResponsavelInclusao(usuarioAutenticado);
		itemBacklogSprint.setSprint(sprint);
		itemBacklogSprint.setItemBacklogProjeto(itemBacklogProjeto);
		itemBacklogSprint.setDataInclusao(dataHoraAtual);

		if (ObjectUtils.isEmpty(itemBacklogSprintUpsertDTO.getSituacao())) {
			itemBacklogSprint.setSituacao(SituacaoItemSprintEnum.A_FAZER);
		}

		itemBacklogSprint = itemBacklogSprintService.save(itemBacklogSprint);

		return modelMapper.map(itemBacklogSprint, ItemBacklogSprintDTO.class);
	}

	public ItemBacklogSprintDTO getById(Long idItemBacklogSprint) throws Exception {
		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		return modelMapper.map(itemBacklogSprint, ItemBacklogSprintDTO.class);
	}

	public ItemBacklogSprintDTO atualizar(Long idItemBacklogSprint,
			ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) throws Exception {

		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);
		itemBacklogSprintValidate.validateSave(itemBacklogSprintUpsertDTO, itemBacklogSprint.getSprint());
		var responsavelRealizacao = usuarioService.getById(itemBacklogSprintUpsertDTO.getIdResponsavelRealizacao());

		itemBacklogSprint.atualizarAtributos(itemBacklogSprintUpsertDTO);
		itemBacklogSprint.setResponsavelRealizacao(responsavelRealizacao);

		itemBacklogSprint = itemBacklogSprintService.save(itemBacklogSprint);
		return modelMapper.map(itemBacklogSprint, ItemBacklogSprintDTO.class);
	}

	public void delete(Long idItemBacklogSprint) throws Exception {
		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		itemBacklogSprintService.delete(idItemBacklogSprint);
	}

	public void repriorizarItemBacklogSprint(Long idItemBacklogSprint, Long valorPrioridade) {
		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var sprint = itemBacklogSprint.getSprint();
		var projeto = sprint.getProjeto();
		
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);
		sprintValidate.validarAlterarDadosSprint(sprint);
		
		itemBacklogSprintService.repriorizarItemBacklogSprint(itemBacklogSprint, valorPrioridade);
		
	}

}
