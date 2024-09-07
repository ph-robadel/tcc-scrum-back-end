package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintPlanningDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.facade.ItemBacklogSprintFacade;
import br.ufes.facade.SprintFacade;
import br.ufes.util.ResponseSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Sprints")
@RequestMapping("sprints")
@SecurityRequirement(name = "token")
@Transactional(rollbackFor = Exception.class)
public class SprintController {

	@Autowired
	private SprintFacade sprintFacade;

	@Autowired
	private ItemBacklogSprintFacade itemBacklogSprintFacade;

	@Operation(summary = "Obter Sprint")
	@GetMapping("/{idSprint}")
	public ResponseEntity<SprintDTO> atualizarSprint(@PathVariable Long idSprint) throws Exception {
		var sprint = sprintFacade.getById(idSprint);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Atualizar Sprint")
	@PutMapping("/{idSprint}")
	public ResponseEntity<SprintDTO> atualizarSprint(@PathVariable Long idSprint,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) throws Exception {
		var sprint = sprintFacade.atualizarSprint(idSprint, sprintUpsertDTO);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Remover sprint")
	@DeleteMapping("/{idSprint}")
	public ResponseEntity<Object> deleteSprint(@PathVariable Long idSprint) throws Exception {
		sprintFacade.cancelarSprint(idSprint);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Adicionar novo item ao backlog de planejamento")
	@PostMapping("/{idSprint}/item-backlog-projeto/{idItemBacklogProjeto}/item-backlog-planejamento")
	public ResponseEntity<ItemBacklogProjetoSimpleDTO> adicionarItemBacklogPlanejamento(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto) throws Exception {

		var itemBacklogPlanejamento = sprintFacade.adicionarItemBacklogPlanejamento(idSprint, idItemBacklogProjeto);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogPlanejamento);
	}

	@Operation(summary = "Remover item do backlog de planejamento")
	@DeleteMapping("/{idSprint}/item-backlog-planejamento/{idItemBacklogProjeto}")
	public ResponseEntity<Object> removerItemBacklogPlanejamento(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto) throws Exception {

		sprintFacade.removerItemBacklogPlanejamento(idSprint, idItemBacklogProjeto);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Buscar itens do backlog de planejamento da sprint")
	@GetMapping("/{idSprint}/item-backlog-planejamento/search")
	public ResponseEntity<ResponseSearch<ItemBacklogProjetoSimpleDTO>> searchItensBacklogPlanejamento(
			@PathVariable("idSprint") Long idSprint, @RequestParam(defaultValue = "") String titulo,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "prioridade") String fieldSort,
			@RequestParam(defaultValue = "ASC") String sortOrder) throws Exception {

		var sprintFilterDTO = new ItemBacklogPlanejamentoFilterDTO(titulo);
		sprintFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var responseSearch = sprintFacade.searchPlanejamento(idSprint, sprintFilterDTO);
		return ResponseEntity.ok(responseSearch);
	}

	@Operation(summary = "Adicionar novo item backlog a sprint")
	@PostMapping("/{idSprint}/item-backlog-projeto/{idItemBacklogProjeto}/item-backlog-sprint")
	public ResponseEntity<ItemBacklogSprintDTO> adicionarItemBacklogSprint(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto, @RequestBody ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO)
			throws Exception {

		var itemBacklogSprint = itemBacklogSprintFacade.adicionarItemBacklogSprint(idSprint, idItemBacklogProjeto,
				itemBacklogSprintUpsertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogSprint);
	}

	@Operation(summary = "Buscar itens backlog da sprint")
	@GetMapping("/{idSprint}/item-backlog-sprint/search")
	public ResponseEntity<ResponseSearch<ItemBacklogSprintBasicDTO>> searchItensBacklogSprint(
			@PathVariable("idSprint") Long idSprint, @RequestParam(defaultValue = "") String titulo,
			@RequestParam(defaultValue = "") String situacao,
			@RequestParam(defaultValue = "") Long idResponsavelRealizacao, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "prioridade") String fieldSort,
			@RequestParam(defaultValue = "ASC") String sortOrder) throws Exception {

		var sprintFilterDTO = new ItemBacklogSprintFilterDTO(titulo, situacao, idResponsavelRealizacao);
		sprintFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var responseSearch = itemBacklogSprintFacade.search(idSprint, sprintFilterDTO);
		return ResponseEntity.ok(responseSearch);
	}
	
	@Operation(summary = "Adicionar planning a sprint")
	@PostMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> adicionarSprintPlanning(@PathVariable Long idSprint,
			@RequestBody SprintPlanningDTO planningDTO)
			throws Exception {

		var planning = sprintFacade.saveSprintPlanning(idSprint, planningDTO, false);
		return ResponseEntity.status(HttpStatus.CREATED).body(planning);
	}
	
	@Operation(summary = "obter planning da sprint")
	@GetMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> getSprintPlanning(@PathVariable Long idSprint) throws Exception {
		
		var planning = sprintFacade.getSprintPlanning(idSprint);
		return ResponseEntity.ok().body(planning);
	}
	
	@Operation(summary = "Atualizar a planning a sprint")
	@PutMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> updateSprintPlanning(@PathVariable Long idSprint, @RequestBody SprintPlanningDTO planningDTO) throws Exception {
		
		var planning = sprintFacade.saveSprintPlanning(idSprint, planningDTO, true);
		return ResponseEntity.ok().body(planning);
	}
	
	@Operation(summary = "Concluir planning da sprint")
	@PostMapping("/{idSprint}/concluir-planning")
	public ResponseEntity<Object> concluirSprintPlanning(@PathVariable Long idSprint) throws Exception {
		
		sprintFacade.concluirSprintPlanning(idSprint);
		return ResponseEntity.ok().body(null);
	}

}
