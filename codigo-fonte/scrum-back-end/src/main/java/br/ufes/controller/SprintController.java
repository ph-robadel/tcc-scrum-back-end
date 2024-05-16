package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.SprintDTO;
import br.ufes.facade.SprintFacade;

@RestController
@RequestMapping("sprints")
public class SprintController {

	@Autowired
	private SprintFacade sprintFacade;

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

}
