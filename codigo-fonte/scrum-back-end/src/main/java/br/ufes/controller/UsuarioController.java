package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.UsuarioUpdateSenhaAdminDTO;
import br.ufes.dto.UsuarioUpdateSenhaDTO;
import br.ufes.dto.UsuarioUpsertDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.facade.UsuarioFacade;
import br.ufes.util.ResponseSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Usuários")
@RequestMapping("usuarios")
@SecurityRequirement(name = "token")
@Transactional(rollbackFor = Exception.class)
public class UsuarioController {

	@Autowired
	private UsuarioFacade usuarioFacade;

	@Operation(summary = "Cadastrar um novo usuário")
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioUpsertDTO usuarioDTO)
			throws Exception {
		var response = usuarioFacade.cadastrarUsuario(usuarioDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Pesquisar usuário")
	@GetMapping
	public ResponseEntity<ResponseSearch<UsuarioResponseDTO>> search(
			@RequestParam(defaultValue = "") String nome,
			@RequestParam(defaultValue = "true") Boolean apenasAtivo,
			@RequestParam(defaultValue = "") String perfil,
			@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String fieldSort,
            @RequestParam(defaultValue = "DESC") String sortOrder) throws Exception {
		
		var usuarioSearch = new UsuarioFilterDTO(nome, apenasAtivo, perfil);
		usuarioSearch.setPageAndSorting(page, size, fieldSort, sortOrder);
		
		var resultSearch = usuarioFacade.search(usuarioSearch);
		return ResponseEntity.ok(resultSearch);
	}

	@Operation(summary = "Obter usuário por id")
	@GetMapping("/{idUsuario}")
	public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable("idUsuario") Long idUsuario)
			throws Exception {
		var usuarioResponseDTO = usuarioFacade.getById(idUsuario);
		return ResponseEntity.ok(usuarioResponseDTO);
	}

	@Operation(summary = "Inativar usuário")
	@DeleteMapping("/{idUsuario}")
	public ResponseEntity<Object> inativarUsuario(@PathVariable("idUsuario") Long idUsuario) throws Exception {
		usuarioFacade.inativarUsuario(idUsuario);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Atualizar senha do próprio usuário logado")
	@PatchMapping("/senha")
	public ResponseEntity<Object> atualizarSenhaUsuario(@RequestBody UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO)
			throws Exception {
		usuarioFacade.atualizarSenhaUsuario(usuarioUpdateSenhaDTO);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Atualizar senha de usuário como administrador")
	@PatchMapping("/senha-admin")
	public ResponseEntity<Object> atualizarSenhaUsuarioByAdmin(@RequestBody UsuarioUpdateSenhaAdminDTO updateSenhaDTO)
			throws Exception {
		usuarioFacade.atualizarSenhaUsuarioByAdmin(updateSenhaDTO);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Atualizar usuário")
	@PutMapping("/{idUsuario}")
	public ResponseEntity<Object> atualizarUsuario(@PathVariable Long idUsuario,
			@RequestBody UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		var usuario = usuarioFacade.atualizarUsuario(idUsuario, usuarioUpdateDTO);
		return ResponseEntity.ok(usuario);
	}

}
