package br.ufes.facade;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.SprintDailyDTO;
import br.ufes.dto.SprintPlanningDTO;
import br.ufes.dto.SprintRetrospectiveDTO;
import br.ufes.dto.SprintReviewDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.entity.ItemBacklogPlanejamento;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.entity.ItemReview;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.SprintDaily;
import br.ufes.entity.SprintPlanning;
import br.ufes.entity.SprintRetrospective;
import br.ufes.entity.SprintReview;
import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.enums.SituacaoProjetoEnum;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ItemBacklogPlanejamentoService;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.ProjetoService;
import br.ufes.services.SprintDailyService;
import br.ufes.services.SprintPlanningService;
import br.ufes.services.SprintRetrospectiveService;
import br.ufes.services.SprintReviewService;
import br.ufes.services.SprintService;
import br.ufes.services.UsuarioService;
import br.ufes.util.DateUtils;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ItemBacklogPlanejamentoValidate;
import br.ufes.validate.ProjetoUsuarioValidate;
import br.ufes.validate.ProjetoValidate;
import br.ufes.validate.SprintDailyValidate;
import br.ufes.validate.SprintPlanningValidate;
import br.ufes.validate.SprintRetrospectiveValidate;
import br.ufes.validate.SprintReviewValidate;
import br.ufes.validate.SprintValidate;

@Component
public class SprintFacade {

	@Autowired
	private SprintService sprintService;

	@Autowired
	private SprintValidate sprintValidate;

	@Autowired
	private SprintPlanningValidate sprintPlanningValidate;

	@Autowired
	private SprintDailyValidate sprintDailyValidate;

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
	private SprintDailyService sprintDailyService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private SprintReviewService sprintReviewService;

	@Autowired
	private SprintRetrospectiveService sprintRetrospectiveService;

	@Autowired
	private SprintReviewValidate sprintReviewValidate;

	@Autowired
	private SprintRetrospectiveValidate sprintRetrospectiveValidate;

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
		sprintValidate.validarAlterarDadosSprint(sprint);
		sprintPlanningValidate.validateSavePlanning(sprint, planningDTO, isUpdate);
		
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());
		var itensPlanejamento = sprint.getBacklogPlanejamento().stream()
				.map(ItemBacklogPlanejamento::getItemBacklogProjeto).collect(Collectors.toList());

		var sprintPlanning = isUpdate ? sprint.getPlanning() : new SprintPlanning();
		sprintPlanning.atualizarAtributos(planningDTO);

		sprintPlanningService.atualizarParticipantesEvento(planningDTO, timeProjeto, sprintPlanning);
		sprintPlanningService.atualizarCapacidadePlanning(planningDTO, timeProjeto, sprintPlanning);
		sprintPlanningService.atualizarItensSelecionadosPlanning(planningDTO, timeProjeto, itensPlanejamento,
				sprintPlanning);

		var planningSave = sprintPlanningService.save(sprintPlanning);

		if (!isUpdate) {
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

		if (ObjectUtils.isEmpty(sprint.getPlanning())) {
			throw new BusinessException("Planning não cadastrada");
		}

		return modelMapper.map(sprint.getPlanning(), SprintPlanningDTO.class);
	}

	public void concluirPlanning(Long idSprint) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintPlanningValidate.validateConcluirPlanning(sprint);

		var itensSprint = itemBacklogSprintService.gerarItensBacklogSprintByPlanning(sprint,
				usuarioService.getUsuarioAutenticado());

		itensSprint.forEach(itemSprint -> {
			var itemProjeto = itemSprint.getItemBacklogProjeto();
			itemProjeto.setSituacao(SituacaoItemProjetoEnum.EM_DESENVOLVIMENTO);
			itemBacklogProjetoService.save(itemProjeto);
		});

		sprint.setSituacao(SituacaoSprintEnum.EM_ANDAMENTO);
		sprintService.save(sprint);
	}

	public SprintDailyDTO insertDaily(Long idSprint, SprintDailyDTO dailyDTO) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);

		sprintDailyValidate.validateSaveDaily(sprint, dailyDTO, false);
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		var sprintDaily = new SprintDaily();
		sprintDaily.atualizarAtributos(dailyDTO);
		sprintDaily.setSprint(sprint);

		sprintDailyService.atualizarParticipantesEvento(dailyDTO, timeProjeto, sprintDaily);
		sprintDailyService.atualizarRegistroDaily(dailyDTO, timeProjeto, sprintDaily);

		var dailySave = sprintDailyService.save(sprintDaily);

		if (ObjectUtils.isArray(sprint.getDailys())) {
			sprint.setDailys(new ArrayList<>());
		}

		sprint.getDailys().add(sprintDaily);
		sprintService.save(sprint);

		return modelMapper.map(dailySave, SprintDailyDTO.class);
	}

	public SprintDailyDTO updateSprintDaily(Long idSprint, Long idDaily, SprintDailyDTO dailyDTO) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);

		dailyDTO.setId(idDaily);
		sprintDailyValidate.validateSaveDaily(sprint, dailyDTO, true);
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		var sprintDaily = sprint.getDailys().stream().filter(daily -> daily.getId().equals(idDaily)).findAny().get();
		sprintDaily.atualizarAtributos(dailyDTO);

		sprintDailyService.atualizarParticipantesEvento(dailyDTO, timeProjeto, sprintDaily);
		sprintDailyService.atualizarRegistroDaily(dailyDTO, timeProjeto, sprintDaily);

		var dailySave = sprintDailyService.save(sprintDaily);

		return modelMapper.map(dailySave, SprintDailyDTO.class);
	}

	public SprintDailyDTO getSprintDaily(Long idSprint, Long idDaily) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintDailyValidate.validarDailyEncontrada(sprint, idDaily);

		var sprintDaily = sprint.getDailys().stream().filter(daily -> daily.getId().equals(idDaily)).findAny().get();

		return modelMapper.map(sprintDaily, SprintDailyDTO.class);
	}

	public ResponseSearch<SprintDailyBasicDTO> searchDaily(Long idSprint, SprintDailyFilterDTO filterDTO) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		return sprintDailyService.search(filterDTO);
	}

	public SprintReviewDTO saveSprintReview(Long idSprint, SprintReviewDTO reviewDTO, boolean isUpdate) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);
		sprintReviewValidate.validateSaveReview(sprint, reviewDTO, isUpdate);
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		var itensSprint = sprint.getBacklog().stream().map(ItemBacklogSprint::getItemBacklogProjeto)
				.collect(Collectors.toList());

		var sprintReview = isUpdate ? sprint.getReview() : new SprintReview();
		sprintReview.atualizarAtributos(reviewDTO);

		sprintReviewService.atualizarParticipantesEvento(reviewDTO, timeProjeto, sprintReview);
		sprintReviewService.atualizarItensReview(reviewDTO, itensSprint, sprintReview);

		var reviewSave = sprintReviewService.save(sprintReview);

		sprint.setReview(reviewSave);
		sprintService.save(sprint);

		return modelMapper.map(reviewSave, SprintReviewDTO.class);
	}

	public SprintReviewDTO getSprintReview(Long idSprint) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		if (ObjectUtils.isEmpty(sprint.getReview())) {
			throw new BusinessException("Review não cadastrada");
		}

		return modelMapper.map(sprint.getReview(), SprintReviewDTO.class);
	}

	public SprintRetrospectiveDTO saveSprintRetrospective(Long idSprint, SprintRetrospectiveDTO retrospectiveDTO,
			boolean isUpdate) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);
		sprintRetrospectiveValidate.validateSaveRetrospective(sprint, retrospectiveDTO, isUpdate);
		var timeProjeto = sprint.getProjeto().getTime().stream().map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		var sprintRetrospective = isUpdate ? sprint.getRetrospective() : new SprintRetrospective();
		sprintRetrospective.atualizarAtributos(retrospectiveDTO);

		sprintRetrospectiveService.atualizarParticipantesEvento(retrospectiveDTO, timeProjeto, sprintRetrospective);

		var retrospectiveSave = sprintRetrospectiveService.save(sprintRetrospective);

		sprint.setRetrospective(retrospectiveSave);
		sprintService.save(sprint);

		return modelMapper.map(retrospectiveSave, SprintRetrospectiveDTO.class);
	}

	public SprintRetrospectiveDTO getSprintRetrospective(Long idSprint) {
		var sprint = sprintService.getById(idSprint);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(sprint.getProjeto().getId());
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());

		if (ObjectUtils.isEmpty(sprint.getRetrospective())) {
			throw new BusinessException("Retrospective não cadastrada");
		}

		return modelMapper.map(sprint.getRetrospective(), SprintRetrospectiveDTO.class);
	}

	public void concluirSprint(Long idSprint) throws Exception {
		var sprint = sprintService.getById(idSprint);
		var idProjeto = sprint.getProjeto().getId();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);
		sprintValidate.validarConclusaoProjeto(sprint);

		sprint.getReview().getItensAprovados().stream().map(ItemReview::getItemBacklogProjeto).forEach(itemProjeto -> {
			itemProjeto.setSituacao(SituacaoItemProjetoEnum.CONCLUIDO);
			itemBacklogProjetoService.save(itemProjeto);
		});

		sprint.setSituacao(SituacaoSprintEnum.CONCLUIDA);
		sprintService.save(sprint);
	}

	public void cancelarSprint(Long idSprint) throws Exception {
		var sprint = sprintService.getById(idSprint);
		var idProjeto = sprint.getProjeto().getId();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		sprintValidate.validarAlterarDadosSprint(sprint);

		sprint.setSituacao(SituacaoSprintEnum.CANCELADA);
		sprintService.save(sprint);
	}

}
