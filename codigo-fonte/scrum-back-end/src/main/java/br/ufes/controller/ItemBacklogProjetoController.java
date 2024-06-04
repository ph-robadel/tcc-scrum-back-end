package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.facade.ItemBacklogProjetoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Item Backlog do Projeto")
@RequestMapping("item-backlog-projeto")
@SecurityRequirement(name = "token")
public class ItemBacklogProjetoController {

	@Autowired
	private ItemBacklogProjetoFacade itemBacklogProjetoFacade;

	@Operation(summary = "Buscar itens do Backlog do Projeto")
	@GetMapping
	public ResponseEntity<ItemBacklogProjetoDTO> search() {
		try {
			var itemBacklog = itemBacklogProjetoFacade.search();
			return ResponseEntity.ok(itemBacklog);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
