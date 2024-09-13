package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.facade.ItemBacklogSprintFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Itens do Backlog da Sprint")
@RequestMapping("item-backlog-sprint")
@SecurityRequirement(name = "token")
@Transactional(rollbackFor = Exception.class)
public class ItemBackLogSprintController {

	@Autowired
	private ItemBacklogSprintFacade itemBacklogSprintFacade;

	@Operation(summary = "Obter item do backlog da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogSprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog da Sprint não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idItemBacklogSprint}")
	public ResponseEntity<ItemBacklogSprintDTO> getById(@PathVariable Long idItemBacklogSprint) throws Exception {
		var sprint = itemBacklogSprintFacade.getById(idItemBacklogSprint);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Atualizar item do backlog da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogSprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog da Sprint não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idItemBacklogSprint}")
	public ResponseEntity<ItemBacklogSprintDTO> atualizar(@PathVariable Long idItemBacklogSprint,
			@RequestBody ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) throws Exception {
		
		var itemBacklogSprintDTO = itemBacklogSprintFacade.atualizar(idItemBacklogSprint, itemBacklogSprintUpsertDTO);
		return ResponseEntity.ok(itemBacklogSprintDTO);
	}

	@Operation(summary = "Remover item do backlog da sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog da Sprint não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@DeleteMapping("/{idItemBacklogSprint}")
	public ResponseEntity<Object> delete(@PathVariable Long idItemBacklogSprint) throws Exception {
		itemBacklogSprintFacade.delete(idItemBacklogSprint);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Repriorizar Item Backlog Sprint", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog da Sprint não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idItemBacklogSprint}/repriorizar/{valorPrioridade}")
	public ResponseEntity<Object> deleteItemBacklogProjeto(@PathVariable Long idItemBacklogSprint, @PathVariable Long valorPrioridade) throws Exception {
		itemBacklogSprintFacade.repriorizarItemBacklogSprint(idItemBacklogSprint, valorPrioridade);
		return ResponseEntity.ok().build();
	}

}
