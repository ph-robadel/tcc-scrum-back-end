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
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.enums.SituacaoItemSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ItemBacklogSprintValidate;
import br.ufes.validate.ProjetoUsuarioValidate;
import br.ufes.validate.ProjetoValidate;

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

		itemBacklogSprintValidate.validateSave(itemBacklogSprintUpsertDTO);
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

		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			itemBacklogSprint.setPendenteAprovacao(false);
			itemBacklogSprint.setResponsavelAprovacao(usuarioAutenticado);
			itemBacklogSprint.setDataAprovacao(dataHoraAtual);
		} else {
			itemBacklogSprint.setPendenteAprovacao(true);
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
		itemBacklogSprintValidate.validateSave(itemBacklogSprintUpsertDTO);
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

		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			itemBacklogSprintService.delete(idItemBacklogSprint);
		} else {
			itemBacklogSprint.setPendenteRemocao(true);
			itemBacklogSprintService.save(itemBacklogSprint);
		}
	}

	public void aprovarInclusao(Long idItemBacklogSprint) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			throw new BusinessException("Apenas Product Owner podem aprovar a inclusão de um item backlog na sprint");
		}

		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		if (itemBacklogSprint.isPendenteAprovacao()) {
			itemBacklogSprint.setDataAprovacao(LocalDateTime.now());
			itemBacklogSprint.setResponsavelAprovacao(usuarioAutenticado);
			itemBacklogSprintService.save(itemBacklogSprint);
		} else {
			throw new BusinessException("Este item backlog da sprint não está pendente de aprovação da inclusão");
		}

	}

	public void recusarInclusao(Long idItemBacklogSprint) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			throw new BusinessException("Apenas Product Owner podem recusar a inclusão de um item backlog na sprint");
		}

		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		if (itemBacklogSprint.isPendenteRemocao()) {
			itemBacklogSprintService.delete(idItemBacklogSprint);
		} else {
			throw new BusinessException("Este item backlog da sprint não está pendente de aprovação para inclusão");
		}
	}

	public void aprovarRemocao(Long idItemBacklogSprint) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			throw new BusinessException("Apenas Product Owner podem aprovar a inclusão de um item backlog na sprint");
		}

		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		if (itemBacklogSprint.isPendenteRemocao()) {
			itemBacklogSprintService.delete(idItemBacklogSprint);
		} else {
			throw new BusinessException("Este item backlog da sprint não está pendente de aprovação para a remoção");
		}
	}

	public void recusarRemocao(Long idItemBacklogSprint) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())) {
			throw new BusinessException("Apenas Product Owner podem recusar a inclusão de um item backlog na sprint");
		}

		var itemBacklogSprint = itemBacklogSprintService.getById(idItemBacklogSprint);
		var projeto = itemBacklogSprint.getSprint().getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projeto);

		if (itemBacklogSprint.isPendenteRemocao()) {
			itemBacklogSprint.setPendenteRemocao(false);
			itemBacklogSprintService.save(itemBacklogSprint);
		} else {
			throw new BusinessException("Este item backlog da sprint não está pendente de aprovação para a remoção");
		}
	}

}
