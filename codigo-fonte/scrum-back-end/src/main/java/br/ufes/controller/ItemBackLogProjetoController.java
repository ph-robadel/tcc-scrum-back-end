package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ProjetoDTO;
import br.ufes.facade.ProjetoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Projetos")
@RequestMapping("projetos")
@SecurityRequirement(name = "token")
public class ItemBackLogProjetoController {

	@Autowired
	private ProjetoFacade projetoFacade;

	@Operation(summary = "Obter todos os projetos do usuário autenticado")
	@GetMapping
	public ResponseEntity<ProjetoDTO> getAllByUserLogin() {
		try {
			var projeto = projetoFacade.getAllByUserLogin();
			return ResponseEntity.ok(projeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
