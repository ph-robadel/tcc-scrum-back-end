package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.SprintDTO;
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

	@Operation(summary = "Obter todas as sprints de um projeto")
	@GetMapping
	public ResponseEntity<SprintDTO> getAllByUserLogin() {
		try {
			var sprint = sprintFacade.getAllByProjeto();
			return ResponseEntity.ok(sprint);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Inserir uma sprints ao projeto")
	@PostMapping
	public ResponseEntity<SprintDTO> insert(@RequestBody SprintDTO dto) {
		try {
			var sprint = sprintFacade.getAllByProjeto();
			return ResponseEntity.status(HttpStatus.CREATED).body(sprint);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
