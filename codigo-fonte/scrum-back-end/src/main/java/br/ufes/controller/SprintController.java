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
import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.SprintDailyDTO;
import br.ufes.dto.SprintPlanningDTO;
import br.ufes.dto.SprintRetrospectiveDTO;
import br.ufes.dto.SprintReviewDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import br.ufes.facade.ItemBacklogSprintFacade;
import br.ufes.facade.SprintFacade;
import br.ufes.util.ResponseSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@Operation(summary = "Obter Sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}")
	public ResponseEntity<SprintDTO> atualizarSprint(@PathVariable Long idSprint) throws Exception {
		var sprint = sprintFacade.getById(idSprint);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Atualizar Sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idSprint}")
	public ResponseEntity<SprintDTO> atualizarSprint(@PathVariable Long idSprint,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) throws Exception {
		var sprint = sprintFacade.atualizarSprint(idSprint, sprintUpsertDTO);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Adicionar novo item ao backlog de planejamento", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogProjetoSimpleDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint e/ou item backlog do projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/item-backlog-projeto/{idItemBacklogProjeto}/item-backlog-planejamento")
	public ResponseEntity<ItemBacklogProjetoSimpleDTO> adicionarItemBacklogPlanejamento(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto) throws Exception {

		var itemBacklogPlanejamento = sprintFacade.adicionarItemBacklogPlanejamento(idSprint, idItemBacklogProjeto);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogPlanejamento);
	}

	@Operation(summary = "Remover item do backlog de planejamento", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint e/ou item backlog do projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@DeleteMapping("/{idSprint}/item-backlog-planejamento/{idItemBacklogProjeto}")
	public ResponseEntity<Object> removerItemBacklogPlanejamento(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto) throws Exception {

		sprintFacade.removerItemBacklogPlanejamento(idSprint, idItemBacklogProjeto);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Buscar itens do backlog de planejamento da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
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

	@Operation(summary = "Adicionar novo item backlog a sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogSprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint e/ou item backlog do projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/item-backlog-projeto/{idItemBacklogProjeto}/item-backlog-sprint")
	public ResponseEntity<ItemBacklogSprintDTO> adicionarItemBacklogSprint(@PathVariable Long idSprint,
			@PathVariable Long idItemBacklogProjeto, @RequestBody ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO)
			throws Exception {

		var itemBacklogSprint = itemBacklogSprintFacade.adicionarItemBacklogSprint(idSprint, idItemBacklogProjeto,
				itemBacklogSprintUpsertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogSprint);
	}

	@Operation(summary = "Buscar itens backlog da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
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
	
	@Operation(summary = "Adicionar planning a sprint", responses = {
			@ApiResponse(responseCode = "201", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintPlanningDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> adicionarSprintPlanning(@PathVariable Long idSprint,
			@RequestBody SprintPlanningDTO planningDTO)
			throws Exception {

		var planning = sprintFacade.saveSprintPlanning(idSprint, planningDTO, false);
		return ResponseEntity.status(HttpStatus.CREATED).body(planning);
	}
	
	@Operation(summary = "obter planning da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintPlanningDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> getSprintPlanning(@PathVariable Long idSprint) throws Exception {
		
		var planning = sprintFacade.getSprintPlanning(idSprint);
		return ResponseEntity.ok().body(planning);
	}
	
	@Operation(summary = "Atualizar a planning a sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintPlanningDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idSprint}/planning")
	public ResponseEntity<SprintPlanningDTO> updateSprintPlanning(@PathVariable Long idSprint, @RequestBody SprintPlanningDTO planningDTO) throws Exception {
		
		var planning = sprintFacade.saveSprintPlanning(idSprint, planningDTO, true);
		return ResponseEntity.ok().body(planning);
	}
	
	@Operation(summary = "Concluir planning da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/concluir-planning")
	public ResponseEntity<Object> concluirSprintPlanning(@PathVariable Long idSprint) throws Exception {
		
		sprintFacade.concluirPlanning(idSprint);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Adicionar nova daily a sprint", responses = {
			@ApiResponse(responseCode = "201", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDailyDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/daily")
	public ResponseEntity<SprintDailyDTO> adicionarSprintDaily(@PathVariable Long idSprint,
			@RequestBody SprintDailyDTO dailyDTO)
			throws Exception {

		var daily = sprintFacade.insertDaily(idSprint, dailyDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(daily);
	}
	
	@Operation(summary = "Obter daily da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDailyDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint e/ou daily não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}/daily/{idDaily}")
	public ResponseEntity<SprintDailyDTO> getDailyPlanning(@PathVariable Long idSprint, @PathVariable Long idDaily) throws Exception {
		
		var daily = sprintFacade.getSprintDaily(idSprint, idDaily);
		return ResponseEntity.ok().body(daily);
	}
	
	@Operation(summary = "Atualizar a daily da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDailyDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint e/ou daily não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idSprint}/daily/{idDaily}")
	public ResponseEntity<SprintDailyDTO> updateSprintDaily(@PathVariable Long idSprint, @PathVariable Long idDaily, @RequestBody SprintDailyDTO dailyDTO) throws Exception {
		
		var daily = sprintFacade.updateSprintDaily(idSprint, idDaily, dailyDTO);
		return ResponseEntity.ok().body(daily);
	}
	
	@Operation(summary = "Pesquisar dailys da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}/daily/search")
	public ResponseEntity<ResponseSearch<SprintDailyBasicDTO>> pesquisarSprintDaily(@PathVariable Long idSprint, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "inicio") String fieldSort,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws Exception {
		
		var filterDTO = new SprintDailyFilterDTO();
		filterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);
		var dailys = sprintFacade.searchDaily(idSprint, filterDTO);
		return ResponseEntity.ok().body(dailys);
	}
	
	@Operation(summary = "Adicionar review a sprint", responses = {
			@ApiResponse(responseCode = "201", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintReviewDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/review")
	public ResponseEntity<SprintReviewDTO> adicionarSprintReview(@PathVariable Long idSprint,
			@RequestBody SprintReviewDTO reviewDTO)
			throws Exception {

		var review = sprintFacade.saveSprintReview(idSprint, reviewDTO, false);
		return ResponseEntity.status(HttpStatus.CREATED).body(review);
	}
	
	@Operation(summary = "obter review da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintReviewDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}/review")
	public ResponseEntity<SprintReviewDTO> getSprintReview(@PathVariable Long idSprint) throws Exception {
		
		var review = sprintFacade.getSprintReview(idSprint);
		return ResponseEntity.ok().body(review);
	}
	
	@Operation(summary = "Atualizar a review a sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintReviewDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idSprint}/review")
	public ResponseEntity<SprintReviewDTO> updateSprintReview(@PathVariable Long idSprint, @RequestBody SprintReviewDTO reviewDTO) throws Exception {
		
		var review = sprintFacade.saveSprintReview(idSprint, reviewDTO, true);
		return ResponseEntity.ok().body(review);
	}
	
	@Operation(summary = "Adicionar retrospective a sprint", responses = {
			@ApiResponse(responseCode = "201", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintRetrospectiveDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/retrospective")
	public ResponseEntity<SprintRetrospectiveDTO> adicionarSprintRetrospective(@PathVariable Long idSprint,
			@RequestBody SprintRetrospectiveDTO retrospectiveDTO)
					throws Exception {
		
		var retrospective = sprintFacade.saveSprintRetrospective(idSprint, retrospectiveDTO, false);
		return ResponseEntity.status(HttpStatus.CREATED).body(retrospective);
	}
	
	@Operation(summary = "obter retrospective da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintRetrospectiveDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idSprint}/retrospective")
	public ResponseEntity<SprintRetrospectiveDTO> getSprintRetrospective(@PathVariable Long idSprint) throws Exception {
		
		var retrospective = sprintFacade.getSprintRetrospective(idSprint);
		return ResponseEntity.ok().body(retrospective);
	}
	
	@Operation(summary = "Atualizar a retrospective a sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintRetrospectiveDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idSprint}/retrospective")
	public ResponseEntity<SprintRetrospectiveDTO> updateSprintRetrospective(@PathVariable Long idSprint, @RequestBody SprintRetrospectiveDTO retrospectiveDTO) throws Exception {
		
		var retrospective = sprintFacade.saveSprintRetrospective(idSprint, retrospectiveDTO, true);
		return ResponseEntity.ok().body(retrospective);
	}
	

	@Operation(summary = "Concluir sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/concluir")
	public ResponseEntity<Object> concluirSprint(@PathVariable Long idSprint)
					throws Exception {
		
		sprintFacade.concluirSprint(idSprint);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Cancelar sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Sprint não encontrada", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idSprint}/cancelar")
	public ResponseEntity<Object> cancelarSprint(@PathVariable Long idSprint)
			throws Exception {
		
		sprintFacade.cancelarSprint(idSprint);
		return ResponseEntity.ok().build();
	}

}
