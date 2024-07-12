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
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.facade.ItemBacklogSprintFacade;
import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "Obter item do backlog da sprint")
	@GetMapping("/{idItemBacklogSprint}")
	public ResponseEntity<ItemBacklogSprintDTO> getById(@PathVariable Long idItemBacklogSprint) throws Exception {
		var sprint = itemBacklogSprintFacade.getById(idItemBacklogSprint);
		return ResponseEntity.ok(sprint);
	}

	@Operation(summary = "Atualizar item do backlog da sprint")
	@PutMapping("/{idItemBacklogSprint}")
	public ResponseEntity<ItemBacklogSprintDTO> atualizar(@PathVariable Long idItemBacklogSprint,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) throws Exception {
		var sprint = itemBacklogSprintFacade.atualizar(idItemBacklogSprint, sprintUpsertDTO);
		return ResponseEntity.ok(sprint);

	}

	@Operation(summary = "Remover item do backlog da sprint")
	@DeleteMapping("/{idItemBacklogSprint}")
	public ResponseEntity<Object> delete(@PathVariable Long idItemBacklogSprint) throws Exception {
		itemBacklogSprintFacade.delete(idItemBacklogSprint);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Aprovar inclusão do item ao backlog da sprint")
	@PostMapping("/{idItemBacklogSprint}/aprovar-inclusao")
	public ResponseEntity<Object> aprovarInclusao(@PathVariable Long idItemBacklogSprint) throws Exception {
		try {
			itemBacklogSprintFacade.aprovarInclusao(idItemBacklogSprint);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Recusar inclusão do item ao backlog da sprint")
	@PostMapping("/{idItemBacklogSprint}/recusar-inclusao")
	public ResponseEntity<Object> recusarInclusao(@PathVariable Long idItemBacklogSprint) throws Exception {
		try {
			itemBacklogSprintFacade.recusarInclusao(idItemBacklogSprint);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Aprovar remoção item do backlog da sprint")
	@PostMapping("/{idItemBacklogSprint}/aprovar-remocao")
	public ResponseEntity<Object> aprovarRemocao(@PathVariable Long idItemBacklogSprint) throws Exception {
		try {
			itemBacklogSprintFacade.aprovarRemocao(idItemBacklogSprint);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Recusar remoção do item ao backlog da sprint")
	@PostMapping("/{idItemBacklogSprint}/recusar-remocao")
	public ResponseEntity<Object> recusarRemocao(@PathVariable Long idItemBacklogSprint) throws Exception {
		itemBacklogSprintFacade.recusarRemocao(idItemBacklogSprint);
		return ResponseEntity.ok().build();
	}

}
