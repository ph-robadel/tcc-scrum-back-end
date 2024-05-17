package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.UsuarioDTO;
import br.ufes.facade.UsuarioFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Usuários")
@RequestMapping("usuarios")
@SecurityRequirement(name = "token")
public class UsuarioController {

	@Autowired
	private UsuarioFacade usuarioFacade;

	@Operation(summary = "Cadastrar um novo usuário")
	@PostMapping
	public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		try {
			usuarioFacade.cadastrarUsuario(usuarioDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
