package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.facade.ItemBacklogSprintFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Item Backlog da Sprint")
@RequestMapping("item-backlog-sprint")
@SecurityRequirement(name = "token")
public class ItemBackLogSprintController {

	@Autowired
	private ItemBacklogSprintFacade itemBacklogSprintFacade;

	@Operation(summary = "Buscar itens do Backlog da Sprint")
	@GetMapping
	public ResponseEntity<ItemBacklogSprintDTO> search() {
		try {
			var itemBacklog = itemBacklogSprintFacade.search();
			return ResponseEntity.ok(itemBacklog);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
