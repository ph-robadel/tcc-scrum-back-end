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

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoUpdateDTO;
import br.ufes.facade.ItemBacklogProjetoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Itens do Backlog do Projeto")
@RequestMapping("item-backlog-projeto")
@SecurityRequirement(name = "token")
@Transactional(rollbackFor = Exception.class)
public class ItemBacklogProjetoController {

	@Autowired
	private ItemBacklogProjetoFacade itemBacklogProjetoFacade;

	@Operation(summary = "Obter item do Backlog do Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog do Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<ItemBacklogProjetoDTO> getById(@PathVariable Long idItemBacklogProjeto) throws Exception {
		var itemBacklogProjeto = itemBacklogProjetoFacade.getById(idItemBacklogProjeto);
		return ResponseEntity.ok(itemBacklogProjeto);
	}

	@Operation(summary = "Atualizar Item Backlog Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog do Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<ItemBacklogProjetoDTO> atualizarItemBacklogProjeto(@PathVariable Long idItemBacklogProjeto,
			@RequestBody ItemBacklogProjetoUpdateDTO itemBacklogProjetoUpsertDTO) throws Exception {
		var itemBacklogProjeto = itemBacklogProjetoFacade.atualizarItemBacklogProjeto(idItemBacklogProjeto,
				itemBacklogProjetoUpsertDTO);
		return ResponseEntity.ok(itemBacklogProjeto);
	}
	
	@Operation(summary = "Repriorizar Item Backlog Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog do Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idItemBacklogProjeto}/repriorizar/{valorPrioridade}")
	public ResponseEntity<Object> deleteItemBacklogProjeto(@PathVariable Long idItemBacklogProjeto, @PathVariable Long valorPrioridade) throws Exception {
		itemBacklogProjetoFacade.repriorizarItemBacklogProjeto(idItemBacklogProjeto, valorPrioridade);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Remover Item Backlog Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Item Backlog do Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@DeleteMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<Object> deleteItemBacklogProjeto(@PathVariable Long idItemBacklogProjeto) throws Exception {
		itemBacklogProjetoFacade.deleteItemBacklogProjeto(idItemBacklogProjeto);
		return ResponseEntity.ok().build();
	}

}
