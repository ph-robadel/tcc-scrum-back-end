package br.ufes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoInsertDTO;
import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoExportDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.SprintBasicDTO;
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
import br.ufes.util.ResponseSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Projetos")
@RequestMapping("projetos")
@SecurityRequirement(name = "token")
@Transactional(rollbackFor = Exception.class)
public class ProjetoController {

	@Autowired
	private ProjetoFacade projetoFacade;

	@Autowired
	private SprintFacade sprintFacade;

	@Autowired
	private ItemBacklogProjetoFacade itemBacklogProjetoFacade;

	@Operation(summary = "Cadastrar um novo projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping
	public ResponseEntity<ProjetoDTO> cadastrarProjeto(@RequestBody ProjetoUpsertDTO projetoInsertDTO)
			throws Exception {
		var projeto = projetoFacade.cadastrarProjeto(projetoInsertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(projeto);
	}

	@Operation(summary = "buscar projetos do usuário autenticado", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/search")
	public ResponseEntity<ResponseSearch<ProjetoBasicDTO>> search(@RequestParam(defaultValue = "") String nome,
			@RequestParam(defaultValue = "true") Boolean apenasAtivo, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String fieldSort,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws Exception {

		var projetoFilterDTO = new ProjetoFilterDTO(nome, apenasAtivo);
		projetoFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var projeto = projetoFacade.search(projetoFilterDTO);
		return ResponseEntity.ok(projeto);
	}

	@Operation(summary = "Obter projeto por id", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idProjeto}")
	public ResponseEntity<ProjetoDTO> getById(@PathVariable Long idProjeto) throws Exception {
		var projeto = projetoFacade.getById(idProjeto);
		return ResponseEntity.ok(projeto);

	}

	@Operation(summary = "Atualizar projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PutMapping("/{idProjeto}")
	public ResponseEntity<ProjetoDTO> atualizarProjeto(@PathVariable Long idProjeto,
			@RequestBody ProjetoUpsertDTO projetoUpdateDTO) throws Exception {
		var projeto = projetoFacade.atualizarProjeto(idProjeto, projetoUpdateDTO);
		return ResponseEntity.ok(projeto);
	}

	@Operation(summary = "Sinalizar conclusão do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/concluir")
	public ResponseEntity<Object> concluirProjeto(@PathVariable Long idProjeto) throws Exception {
		projetoFacade.concluirProjeto(idProjeto);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Sinalizar cancelamento do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/cancelar")
	public ResponseEntity<Object> cancelarProjeto(@PathVariable Long idProjeto) throws Exception {
		projetoFacade.cancelarProjeto(idProjeto);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Ativar projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/reativar")
	public ResponseEntity<Object> reativarProjeto(@PathVariable Long idProjeto) throws Exception {
		projetoFacade.reativarProjeto(idProjeto);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Adicionar novo usuário ao projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto e/ou usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/usuarios/{idUsuario}")
	public ResponseEntity<Object> cadastrarProjetoUsuario(@PathVariable("idProjeto") Long idProjeto,
			@PathVariable("idUsuario") Long idUsuario) throws Exception {
		projetoFacade.cadastrarProjetoUsuario(idProjeto, idUsuario);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "Inativar usuário do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto e/ou usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/usuarios/{idUsuario}/inativar")
	public ResponseEntity<Object> inativarProjetoUsuario(@PathVariable("idProjeto") Long idProjeto,
			@PathVariable("idUsuario") Long idUsuario) throws Exception {
		projetoFacade.inativarProjetoUsuario(idProjeto, idUsuario);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Reativar usuário do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content()),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto e/ou usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/usuarios/{idUsuario}/reativar")
	public ResponseEntity<Object> ativarProjetoUsuario(@PathVariable("idProjeto") Long idProjeto,
			@PathVariable("idUsuario") Long idUsuario) throws Exception {
		projetoFacade.reativarProjetoUsuario(idProjeto, idUsuario);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Buscar usuários do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idProjeto}/usuarios/search")
	public ResponseEntity<ResponseSearch<UsuarioResponseDTO>> searchProjetoUsuario(@PathVariable Long idProjeto,
			@RequestParam(defaultValue = "") String nomeUsuario, @RequestParam(defaultValue = "") String perfil,
			@RequestParam(defaultValue = "true") Boolean apenasAtivo, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String fieldSort,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws Exception {

		var projetoFilterDTO = new ProjetoUsuarioFilterDTO(nomeUsuario, perfil, apenasAtivo);
		projetoFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var projeto = projetoFacade.searchProjetoUsuario(idProjeto, projetoFilterDTO);
		return ResponseEntity.ok(projeto);
	}

	@Operation(summary = "Cadastrar um novo Item Backlog Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ItemBacklogProjetoDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/item-backlog-projeto")
	public ResponseEntity<ItemBacklogProjetoDTO> cadastrarItemBacklogProjeto(@PathVariable Long idProjeto,
			@RequestBody ItemBacklogProjetoInsertDTO projetoInsertDTO) throws Exception {

		var itemBacklogProjeto = itemBacklogProjetoFacade.cadastrarItemBacklogProjeto(idProjeto, projetoInsertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(itemBacklogProjeto);

	}

	@Operation(summary = "Buscar itens do Backlog do Projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idProjeto}/item-backlog-projeto/search")
	public ResponseEntity<ResponseSearch<ItemBacklogProjetoBasicDTO>> searchItemBacklogProjeto(
			@PathVariable Long idProjeto, @RequestParam(defaultValue = "") String titulo,
			@RequestParam(defaultValue = "") String codigo, @RequestParam(defaultValue = "") String situacao,
			@RequestParam(defaultValue = "") String categoria, @RequestParam(defaultValue = "") Long idAutor,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "prioridade") String fieldSort,
			@RequestParam(defaultValue = "ASC") String sortOrder) throws Exception {

		var itemProjetoFilterDTO = new ItemBacklogProjetoFilterDTO(titulo, codigo, situacao, categoria, idAutor);
		itemProjetoFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var itemBacklogProjetoDTO = itemBacklogProjetoFacade.search(idProjeto, itemProjetoFilterDTO);
		return ResponseEntity.ok(itemBacklogProjetoDTO);

	}

	@Operation(summary = "Cadastrar uma nova sprint ao projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = SprintDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@PostMapping("/{idProjeto}/sprints")
	public ResponseEntity<SprintDTO> cadastrarSprint(@PathVariable("idProjeto") Long idProjeto,
			@RequestBody SprintUpsertDTO sprintUpsertDTO) throws Exception {

		var sprintDTO = sprintFacade.cadastrarSprint(idProjeto, sprintUpsertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(sprintDTO);

	}

	@Operation(summary = "Buscar sprints do projeto", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = ResponseSearch.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Projeto não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idProjeto}/sprints/search")
	public ResponseEntity<ResponseSearch<SprintBasicDTO>> searchSprint(@PathVariable("idProjeto") Long idProjeto,
			@RequestParam(defaultValue = "") Integer numero, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String fieldSort,
			@RequestParam(defaultValue = "ASC") String sortOrder) throws Exception {
		
		var sprintFilterDTO = new SprintFilterDTO(numero);
		sprintFilterDTO.setPageAndSorting(page, size, fieldSort, sortOrder);

		var itemBacklogProjetoDTO = sprintFacade.search(idProjeto, sprintFilterDTO);
		return ResponseEntity.ok(itemBacklogProjetoDTO);

	}
	
	@Operation(summary = "Exportar dados projeto - formato JSON", responses = {
			@ApiResponse(responseCode = "200", description = "Sucesso na requisição", content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
			@ApiResponse(responseCode = "400", ref = "badRequest"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "403", ref = "forbidden"),
			@ApiResponse(responseCode = "500", ref = "internalServerError") })
	@GetMapping("/{idProjeto}/exportar-dados")
	public ResponseEntity<ProjetoExportDTO> exportarProjeto(@PathVariable("idProjeto") Long idProjeto) throws Exception {
		var projeto = projetoFacade.exportarProjeto(idProjeto);
		return ResponseEntity.ok(projeto);
	}

}
