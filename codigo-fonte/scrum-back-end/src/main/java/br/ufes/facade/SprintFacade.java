package br.ufes.facade;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.entity.Sprint;
import br.ufes.services.ItemBacklogSprintService;
import br.ufes.services.ProjetoService;
import br.ufes.services.SprintService;
import br.ufes.util.DateUtils;
import br.ufes.util.ResponseSearch;
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

		sprint = sprintService.save(sprint);

		itemBacklogSprintService.criarItensNovaSprint(sprint);

		return modelMapper.map(sprint, SprintDTO.class);
	}

	public ResponseSearch<SprintBasicDTO> search(Long idProjeto, SprintFilterDTO sprintFiltroDTO) throws Exception {
		sprintFiltroDTO.setIdProjeto(sprintFiltroDTO.getIdProjeto());
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(projetoService.getById(idProjeto));
		
		return sprintService.search(sprintFiltroDTO);
	}

	public SprintDTO atualizarSprint(Long idSprint, SprintUpsertDTO sprintUpsertDTO) throws Exception {
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

	public void deleteSprint(Long idSprint) throws Exception {
		var sprint = sprintService.getById(idSprint);
		var idProjeto = sprint.getProjeto().getId();
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		projetoValidate.validateProjetoAtivo(sprint.getProjeto());
		
//		TODO: Remover itens da sprint

		sprintService.remove(sprint.getId());
	}

}
