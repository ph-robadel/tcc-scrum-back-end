package br.ufes.facade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.SprintDailyDTO;
import br.ufes.dto.SprintPlanningDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.entity.ItemBacklogPlanejamento;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.SprintPlanning;
import br.ufes.enums.SituacaoItemSprintEnum;
import br.ufes.enums.SituacaoProjetoEnum;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ItemBacklogPlanejamentoService;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.ProjetoService;
import br.ufes.services.SprintPlanningService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;
import br.ufes.util.DateUtils;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ItemBacklogPlanejamentoValidate;
import br.ufes.validate.ProjetoUsuarioValidate;
import br.ufes.validate.ProjetoValidate;
import br.ufes.validate.SprintValidate;

@Component
public class SprintFacade {

	@Autowired
	private SprintService sprintService;

	@Autowired
	private SprintValidate sprintValidate;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private ProjetoValidate projetoValidate;

	@Autowired
	private ProjetoUsuarioValidate projetoUsuarioValidate;

	@Autowired
	private ItemBacklogSprintService itemBacklogSprintService;

	@Autowired
	private ItemBacklogProjetoService itemBacklogProjetoService;

	@Autowired
	private ItemBacklogPlanejamentoValidate itemBacklogPlanejamentoValidate;

	@Autowired
	private ItemBacklogPlanejamentoService itemBacklogPlanejamentoService;

	@Autowired
	private SprintPlanningService sprintPlanningService;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ModelMapper modelMapper;

	public SprintDTO getById(Long idSprint) throws Exception {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		return modelMapper.map(sprint, SprintDTO.class);
	}

	public SprintDTO cadastrarSprint(Long idProjeto, SprintUpsertDTO sprintUpsertDTO) throws Exception {
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		var projeto = projetoService.getById(idProjeto);
		projetoValidate.validateProjetoAtivo(projeto);
		sprintValidate.validateSave(projeto, sprintUpsertDTO, null);

		var sprint = new Sprint();
		sprint.setDataInicio(sprintUpsertDTO.getDataInicio());
		sprint.setDataFim(DateUtils.adicionarDiasUteis(sprint.getDataInicio(), projeto.getQuantidadeDiasSprint()));
		sprint.setProjeto(projeto);
		sprint.setNumero(sprintService.getProximoNumeroSprintFromProjeto(projeto.getId()));
		sprint.setSituacao(SituacaoSprintEnum.CRIADA);

		sprint = sprintService.save(sprint);

		if (SituacaoProjetoEnum.NOVO.equals(projeto.getSituacao())) {
			projeto.setSituacao(SituacaoProjetoEnum.EM_ANDAMENTO);
			projetoService.save(projeto);
		}

		itemBacklogSprintService.criarItensNovaSprint(sprint);

		return modelMapper.map(sprint, SprintDTO.class);
	}

	public ResponseSearch<SprintBasicDTO> search(Long idProjeto, SprintFilterDTO sprintFiltroDTO) throws Exception {
		sprintFiltroDTO.setIdProjeto(idProjeto);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(projetoService.getById(idProjeto));

		return sprintService.search(sprintFiltroDTO);
	}

	public SprintDTO atualizarSprint(Long idSprint, SprintUpsertDTO sprintUpsertDTO) {
		var sprint = sprintService.getById(idSprint);
		var idProjeto = sprint.getProjeto().getId();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		var projeto = projetoService.getById(idProjeto);
		sprintValidate.validateSave(projeto, sprintUpsertDTO, sprint.getNumero());

		sprint.setDataInicio(sprintUpsertDTO.getDataInicio());
		sprint.setDataFim(DateUtils.adicionarDiasUteis(sprint.getDataInicio(), projeto.getQuantidadeDiasSprint()));

		sprint = sprintService.save(sprint);
		return modelMapper.map(sprint, SprintDTO.class);
	}

	public void cancelarSprint(Long idSprint) throws Exception {
		var sprint = sprintService.getById(idSprint);
		var idProjeto = sprint.getProjeto().getId();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		sprint.setSituacao(SituacaoSprintEnum.CANCELADA);
		sprintService.save(sprint);
	}

	public ItemBacklogProjetoSimpleDTO adicionarItemBacklogPlanejamento(Long idSprint, Long idItemBacklogProjeto)
			throws BusinessException {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());

		var itemBacklogProjeto = itemBacklogProjetoService.getById(idItemBacklogProjeto);
		itemBacklogPlanejamentoValidate.validateSave(sprint, itemBacklogProjeto);

		var itemBacklogPlanejamento = new ItemBacklogPlanejamento(sprint, itemBacklogProjeto);
		itemBacklogPlanejamentoService.save(itemBacklogPlanejamento);

		return null;
	}

	public void removerItemBacklogPlanejamento(Long idSprint, Long idItemBacklogProjeto) {
		var sprint = sprintService.getById(idSprint);
		var itemBacklogProjeto = itemBacklogProjetoService.getById(idItemBacklogProjeto);
		itemBacklogPlanejamentoValidate.validateDelete(sprint, itemBacklogProjeto);
		itemBacklogPlanejamentoService.delete(idSprint, idItemBacklogProjeto);
	}

	public ResponseSearch<ItemBacklogProjetoSimpleDTO> searchPlanejamento(Long idSprint,
			ItemBacklogPlanejamentoFilterDTO filterDTO) {

		var sprint = sprintService.getById(idSprint);
		var projeto = sprint.getProjeto();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(projeto.getId());
		projetoValidate.validateProjetoAtivo(projetoService.getById(projeto.getId()));

		filterDTO.setIdSprint(idSprint);
		return itemBacklogPlanejamentoService.search(filterDTO);
	}

	public SprintPlanningDTO saveSprintPlanning(Long idSprint, SprintPlanningDTO planningDTO, boolean isUpdate) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validateSavePlanning(sprint, planningDTO, isUpdate);
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());
		var itensPlanejamento = sprint.getBacklogPlanejamento().stream()
				.map(ItemBacklogPlanejamento::getItemBacklogProjeto).collect(Collectors.toList());

		var sprintPlanning = isUpdate ? sprint.getPlanning() : new SprintPlanning();
		sprintPlanning.atualizarAtributos(planningDTO);

		sprintPlanningService.atualizarParticipantesEvento(planningDTO, timeProjeto, sprintPlanning);
		sprintPlanningService.atualizarCapacidadePlanning(planningDTO, timeProjeto, sprintPlanning);
		sprintPlanningService.atualizarItensSelecionadosPlanning(planningDTO, timeProjeto, itensPlanejamento, sprintPlanning);

		var planningSave = sprintPlanningService.save(sprintPlanning);

		if(!isUpdate) {
			sprint.setSituacao(SituacaoSprintEnum.EM_PLANEJAMENTO);
		}
		sprint.setPlanning(planningSave);
		sprintService.save(sprint);

		return modelMapper.map(planningSave, SprintPlanningDTO.class);
	}

	public SprintPlanningDTO getSprintPlanning(Long idSprint) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		return modelMapper.map(sprint.getPlanning(), SprintPlanningDTO.class);
	}

	public void concluirSprintPlanning(Long idSprint) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validateConcluirPlanning(sprint);	
		
		gerarItensBacklogSprintByPlanning(sprint);

		sprint.setSituacao(SituacaoSprintEnum.EM_ANDAMENTO);
		sprintService.save(sprint);
	}

	private void gerarItensBacklogSprintByPlanning(Sprint sprint) {
		var dataHoraAtual = LocalDateTime.now();
		
		for(var item: sprint.getPlanning().getItensSelecionados()) {
			var itemBacklogSprint = new ItemBacklogSprint();
			var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
			var prioridadeNovoItem = itemBacklogSprintService.obterNumeroPrioridadeNovoItem(sprint.getId());

			itemBacklogSprint.setPrioridade(prioridadeNovoItem);
			itemBacklogSprint.setDescricao(item.getDescricao());
			itemBacklogSprint.setHorasEstimadas(item.getHorasEstimadas());
			itemBacklogSprint.setHorasRealizadas(BigDecimal.ZERO);
			itemBacklogSprint.setResponsavelRealizacao(item.getResponsavelRealizacao());
			itemBacklogSprint.setResponsavelInclusao(usuarioAutenticado);
			itemBacklogSprint.setSprint(sprint);
			itemBacklogSprint.setItemBacklogProjeto(item.getItemBacklogProjeto());
			itemBacklogSprint.setDataInclusao(dataHoraAtual);
			itemBacklogSprint.setSituacao(SituacaoItemSprintEnum.A_FAZER);
			itemBacklogSprint = itemBacklogSprintService.save(itemBacklogSprint);
		}
	}

	public SprintDailyDTO insertSprintDaily(Long idSprint, SprintDailyDTO dailyDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	public SprintDailyDTO getSprintDaily(Long idSprint, Long idDaily) {
		// TODO Auto-generated method stub
		return null;
	}

	public SprintPlanningDTO updateSprintPlanning(Long idSprint, Long idDaily, SprintDailyDTO dailyDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponseSearch<SprintDailyBasicDTO> SprintDailyBasicDTO(Long idSprint, SprintDailyFilterDTO filterDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
