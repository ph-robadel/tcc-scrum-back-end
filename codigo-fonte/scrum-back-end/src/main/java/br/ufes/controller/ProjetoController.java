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

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoUpsertDTO;
import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.SprintDTO;
import br.ufes.dto.SprintUpsertDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.facade.ItemBacklogProjetoFacade;
import br.ufes.facade.ProjetoFacade;
import br.ufes.facade.SprintFacade;
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

	@Autowired
	private SprintFacade sprintFacade;

	@Autowired
	private ItemBacklogProjetoFacade itemBacklogProjetoFacade;

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
			var responseSearch = projetoFacade.searchProjetoUsuario(idProjeto, projetoUsuarioFiltroDTO);
			return ResponseEntity.ok(responseSearch);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Adicionar novo usuário ao projeto")
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

	@Operation(summary = "remover usuário do projeto")
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

	@Operation(summary = "Cadastrar um novo Item Backlog Projeto")
	@PostMapping("/{idProjeto}/item-backlog-projeto")
	public ResponseEntity<ItemBacklogProjetoDTO> cadastrarItemBacklogProjeto(
			@RequestBody ItemBacklogProjetoUpsertDTO projetoInsertDTO) {
		try {
			var itemBacklogProjeto = itemBacklogProjetoFacade.cadastrarItemBacklogProjeto(projetoInsertDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogProjeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Buscar itens do Backlog do Projeto")
	@PostMapping("/{idProjeto}/item-backlog-projeto/search")
	public ResponseEntity<ResponseSearch<ItemBacklogProjetoDTO>> searchItemBacklogProjeto(@PathVariable Long idProjeto,
			@RequestBody ItemBacklogProjetoFilterDTO filterDTO) {
		try {
			var itemBacklogProjeto = itemBacklogProjetoFacade.search(idProjeto, filterDTO);
			return ResponseEntity.ok(itemBacklogProjeto);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@Operation(summary = "Cadastrar uma nova sprint ao projeto")
	@PostMapping("/{idProjeto}/sprints")
	public ResponseEntity<SprintDTO> cadastrarSprint(@PathVariable("idProjeto") Long idProjeto,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) {
		try {
			var sprintDTO = sprintFacade.cadastrarSprint(idProjeto, sprintUpsertDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(sprintDTO);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Operation(summary = "Buscar sprints do projeto")
	@PostMapping("/{idProjeto}/sprints/search")
	public ResponseEntity<ResponseSearch<SprintDTO>> searchSprint(@PathParam("idProjeto") Long idProjeto,
			@RequestBody SprintFilterDTO sprintFiltroDTO) {
		try {
			var responseSearch = sprintFacade.search(idProjeto, sprintFiltroDTO);
			return ResponseEntity.ok(responseSearch);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
