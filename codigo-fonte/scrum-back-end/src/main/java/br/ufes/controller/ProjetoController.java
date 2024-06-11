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

import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.facade.ProjetoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import util.ResponseSearch;

@RestController
@Tag(name = "Projetos")
@RequestMapping("projetos")
@SecurityRequirement(name = "token")
public class ProjetoController {

	@Autowired
	private ProjetoFacade projetoFacade;

	@Operation(summary = "Cadastrar um novo projeto")
	@PostMapping
	public ResponseEntity<ProjetoDTO> cadastrarProjeto(@RequestBody ProjetoUpsertDTO projetoInsertDTO) {
		try {
			var projeto = projetoFacade.cadastrarProjeto(projetoInsertDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(projeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Atualizar projeto")
	@PutMapping("/{idProjeto}")
	public ResponseEntity<ProjetoDTO> atualizarProjeto(@PathVariable Long idProjeto,
			@RequestBody ProjetoUpsertDTO projetoUpdateDTO) {
		try {
			var projeto = projetoFacade.atualizarProjeto(idProjeto, projetoUpdateDTO);
			return ResponseEntity.ok(projeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "buscar projetos do usuário autenticado")
	@PostMapping("/search")
	public ResponseEntity<ResponseSearch<ProjetoDTO>> search(@RequestBody ProjetoFilterDTO filterDTO) {
		try {
			var projeto = projetoFacade.search(filterDTO);
			return ResponseEntity.ok(projeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Obter projeto por id")
	@GetMapping("/{idProjeto}")
	public ResponseEntity<ProjetoDTO> getById(@PathVariable Long idProjeto) {
		try {
			var projeto = projetoFacade.getById(idProjeto);
			return ResponseEntity.ok(projeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Inativar projeto")
	@DeleteMapping("/{idProjeto}")
	public ResponseEntity<Object> inativarProjeto(@PathVariable Long idProjeto) {
		try {
			projetoFacade.inativarProjeto(idProjeto);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Buscar usuários do projeto")
	@PostMapping("/{idProjeto}/usuarios/search")
	public ResponseEntity<ResponseSearch<UsuarioResponseDTO>> searchProjetoUsuario(
			@PathParam("idProjeto") Long idProjeto, @RequestBody ProjetoUsuarioFilterDTO projetoUsuarioFiltroDTO) {
		try {
			projetoFacade.inativarProjeto(idProjeto);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Cadastrar usuário do projeto")
	@PostMapping("/{idProjeto}/usuarios/{idUsuario}")
	public ResponseEntity<Object> cadastrarProjetoUsuario(@PathVariable("idProjeto") Long idProjeto,
			@PathVariable("idUsuario") Long idUsuario) {
		try {
			projetoFacade.cadastrarProjetoUsuario(idProjeto, idUsuario);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Inativar usuário do projeto")
	@DeleteMapping("/{idProjeto}/usuarios/{idUsuario}")
	public ResponseEntity<Object> inativarProjetoUsuario(@PathVariable("idProjeto") Long idProjeto,
			@PathVariable("idUsuario") Long idUsuario) {
		try {
			projetoFacade.inativarProjetoUsuario(idProjeto, idUsuario);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
