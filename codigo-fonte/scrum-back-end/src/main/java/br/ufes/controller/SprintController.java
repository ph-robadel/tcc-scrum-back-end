package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.facade.SprintFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Sprints")
@RequestMapping("sprints")
@SecurityRequirement(name = "token")
public class SprintController {

	@Autowired
	private SprintFacade sprintFacade;

	@Operation(summary = "Atualizar Sprint")
	@PutMapping("/{idSprint}")
	public ResponseEntity<SprintDTO> atualizarSprint(@PathVariable Long idSprint,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) {
		try {
			var sprint = sprintFacade.atualizarSprint(idSprint, sprintUpsertDTO);
			return ResponseEntity.ok(sprint);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Remover sprint")
	@DeleteMapping("/{idSprint}")
	public ResponseEntity<Object> deleteSprint(@PathVariable Long idSprint) {
		try {
			sprintFacade.deleteSprint(idSprint);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
