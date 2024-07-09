package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoUpsertDTO;
import br.ufes.facade.ItemBacklogProjetoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Itens do Backlog do Projeto")
@RequestMapping("item-backlog-projeto")
@SecurityRequirement(name = "token")
public class ItemBacklogProjetoController {

	@Autowired
	private ItemBacklogProjetoFacade itemBacklogProjetoFacade;

	@Operation(summary = "Obter item do Backlog do Projeto")
	@GetMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<ItemBacklogProjetoDTO> getById(@PathVariable Long idItemBacklogProjeto) {
		try {
			var itemBacklogProjeto = itemBacklogProjetoFacade.getById(idItemBacklogProjeto);
			return ResponseEntity.ok(itemBacklogProjeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Atualizar Item Backlog Projeto")
	@PutMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<ItemBacklogProjetoDTO> atualizarItemBacklogProjeto(@PathVariable Long idItemBacklogProjeto,
			@RequestBody ItemBacklogProjetoUpsertDTO itemBacklogProjetoUpsertDTO) {
		try {
			var itemBacklogProjeto = itemBacklogProjetoFacade.atualizarItemBacklogProjeto(idItemBacklogProjeto,
					itemBacklogProjetoUpsertDTO);
			return ResponseEntity.ok(itemBacklogProjeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Remover Item Backlog Projeto")
	@DeleteMapping("/{idItemBacklogProjeto}")
	public ResponseEntity<Object> deleteItemBacklogProjeto(@PathVariable Long idItemBacklogProjeto) {
		try {
			itemBacklogProjetoFacade.deleteItemBacklogProjeto(idItemBacklogProjeto);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
