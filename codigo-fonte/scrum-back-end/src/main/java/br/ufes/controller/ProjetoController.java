package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ProjetoDTO;
import br.ufes.facade.ProjetoFacade;

@RestController
@RequestMapping("projetos")
public class ProjetoController {

	@Autowired
	private ProjetoFacade projetoFacade;

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
