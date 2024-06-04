package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.UsuarioDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.UsuarioUpdateSenhaAdminDTO;
import br.ufes.dto.UsuarioUpdateSenhaDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.facade.UsuarioFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import util.ResponseSearch;

@RestController
@Tag(name = "Usuários")
@RequestMapping("usuarios")
@SecurityRequirement(name = "token")
public class UsuarioController {

	@Autowired
	private UsuarioFacade usuarioFacade;

	@Operation(summary = "Cadastrar um novo usuário")
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		try {
			var response = usuarioFacade.cadastrarUsuario(usuarioDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Buscar usuário")
	@GetMapping("/search")
	public ResponseEntity<ResponseSearch<UsuarioResponseDTO>> search(@RequestBody UsuarioFilterDTO filterDTO) {
		try {
			var resultSearch = usuarioFacade.search(filterDTO);
			return ResponseEntity.ok().body(resultSearch);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Obter dados do usuário por id")
	@GetMapping("/{idUsuario}")
	public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@PathVariable("idUsuario") Long idUsuario) {
		try {
			var usuarioResponseDTO = usuarioFacade.getById(idUsuario);
			return ResponseEntity.ok().body(usuarioResponseDTO);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Inativar usuário")
	@DeleteMapping("/{idUsuario}")
	public ResponseEntity<Object> inativarUsuario(@PathVariable("idUsuario") Long idUsuario) {
		try {
			usuarioFacade.inativarUsuario(idUsuario);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Atualizar senha do próprio usuário logado")
	@PutMapping("/senha")
	public ResponseEntity<Object> atualizarSenhaUsuario(@RequestBody UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO) {
		try {
			usuarioFacade.atualizarSenhaUsuario(usuarioUpdateSenhaDTO);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Atualizar senha de usuário pelo administrador")
	@PutMapping("/senha-admin")
	public ResponseEntity<Object> atualizarSenhaUsuarioByAdmin(@RequestBody UsuarioUpdateSenhaAdminDTO updateSenhaDTO) {
		try {
			usuarioFacade.atualizarSenhaUsuarioByAdmin(updateSenhaDTO);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
