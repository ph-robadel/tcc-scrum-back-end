package br.ufes.facade;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoExportDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.ProjetoUsuarioDTO;
import br.ufes.dto.SprintExportDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.entity.Projeto;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.enums.SituacaoProjetoEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ProjetoService;
import br.ufes.services.ProjetoUsuarioService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ProjetoUsuarioValidate;
import br.ufes.validate.ProjetoValidate;

@Component
public class ProjetoFacade {

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProjetoUsuarioService projetoUsuarioService;

	@Autowired
	private ProjetoValidate projetoValidate;

	@Autowired
	private ProjetoUsuarioValidate projetoUsuarioValidate;

	@Autowired
	private ModelMapper modelMapper;

	public ProjetoDTO cadastrarProjeto(ProjetoUpsertDTO projetoInsertDTO) throws Exception {
		projetoValidate.validateSave(projetoInsertDTO);

		var projeto = modelMapper.map(projetoInsertDTO, Projeto.class);
		projeto.setSituacao(SituacaoProjetoEnum.NOVO);
		projeto = projetoService.save(projeto);

		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();

		this.cadastrarProjetoUsuario(projeto.getId(), usuarioAutenticado.getId());
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public ResponseSearch<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO) throws Exception {
		var usuario = usuarioService.getUsuarioAutenticado();
		filterDTO.setIdUsuario(usuario.getId());
		filterDTO.setIsAdmin(PerfilUsuarioEnum.ADMINISTRADOR.equals(usuario.getPerfil()));

		return projetoService.search(filterDTO);
	}

	public ProjetoDTO getById(Long idProjeto) throws Exception {
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);

		var projeto = projetoService.getById(idProjeto);
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public void concluirProjeto(Long idProjeto) throws Exception {
		projetoUsuarioValidate.validarAcessoAdminOuUsuarioAutenticadoAoProjeto(idProjeto);
		var projeto = projetoService.getById(idProjeto);

		if (!projeto.isAtivo()) {
			throw new BusinessException("Projeto não está " + projeto.getSituacao().getSituacao());
		}

		projeto.setSituacao(SituacaoProjetoEnum.CONCLUIDO);
		projetoService.save(projeto);
	}

	public void cancelarProjeto(Long idProjeto) throws Exception {
		projetoUsuarioValidate.validarAcessoAdminOuUsuarioAutenticadoAoProjeto(idProjeto);
		var projeto = projetoService.getById(idProjeto);

		if (!projeto.isAtivo()) {
			throw new BusinessException("Projeto não está em andamento");
		}

		projeto.setSituacao(SituacaoProjetoEnum.CANCELADO);
		projetoService.save(projeto);
	}

	public void reativarProjeto(Long idProjeto) throws Exception {
		projetoUsuarioValidate.validarAcessoAdminOuUsuarioAutenticadoAoProjeto(idProjeto);

		var projeto = projetoService.getById(idProjeto);

		if (projeto.isAtivo()) {
			throw new BusinessException("Projeto já está em andamento");
		}

		projeto.setSituacao(SituacaoProjetoEnum.EM_ANDAMENTO);
		projetoService.save(projeto);
	}

	public ProjetoDTO atualizarProjeto(Long idProjeto, ProjetoUpsertDTO projetoInsertDTO) throws Exception {
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		var projeto = projetoService.getById(idProjeto);
		projetoValidate.validateProjetoAtivo(projeto);
		projetoValidate.validateSave(projetoInsertDTO);

		projeto.atualizarAtributos(projetoInsertDTO);

		projeto = projetoService.save(projeto);
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public ResponseSearch<UsuarioResponseDTO> searchProjetoUsuario(Long idProjeto, ProjetoUsuarioFilterDTO filterDTO)
			throws Exception {
		projetoUsuarioValidate.validarAcessoAdminOuUsuarioAutenticadoAoProjeto(idProjeto);

		var projeto = projetoService.getById(idProjeto);
		if (!projeto.isAtivo()) {
			throw new BusinessException("Projeto inativo");
		}

		filterDTO.setIdProjeto(idProjeto);

		return projetoUsuarioService.search(filterDTO);
	}

	public void cadastrarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
		var projeto = projetoService.getById(idProjeto);
		var usuario = usuarioService.getById(idUsuario);
		
		projetoValidate.validateProjetoAtivo(projeto);
		projetoUsuarioValidate.validateSave(projeto, usuario);

		var projetoUsuario = new ProjetoUsuario(projeto, usuario);
		projetoUsuarioService.save(projetoUsuario);
	}

	public void inativarProjetoUsuario(Long idProjeto, Long idUsuario) {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioAutenticado.getPerfil())) {
			projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		}

		var projetoUsuario = projetoUsuarioService.getByIdProjetoAndIdUsuario(idProjeto, idUsuario);
		projetoValidate.validateProjetoAtivo(projetoUsuario.getProjeto());
		
		if (ObjectUtils.isEmpty(projetoUsuario)) {
			throw new BusinessException("O usuário não está cadastrado ao projeto informado");
		} else if (!projetoUsuario.isAtivo()) {
			throw new BusinessException("O usuário já está inativo neste projeto");
		}

		projetoUsuario.setAtivo(false);
		projetoUsuarioService.save(projetoUsuario);
	}

	public void reativarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioAutenticado.getPerfil())) {
			projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		}

		var projetoUsuario = projetoUsuarioService.getByIdProjetoAndIdUsuario(idProjeto, idUsuario);
		projetoValidate.validateProjetoAtivo(projetoUsuario.getProjeto());

		if (ObjectUtils.isEmpty(projetoUsuario)) {
			throw new BusinessException("O usuário não está cadastrado ao projeto informado");
		} else if (projetoUsuario.isAtivo()) {
			throw new BusinessException("O usuário já está ativo neste projeto");
		}

		projetoUsuario.setAtivo(true);
		projetoUsuarioService.save(projetoUsuario);
	}

	public ProjetoExportDTO exportarProjeto(Long idProjeto) {
//		TODO: Configurar modelMapper para resolver problema de PersistentBag to List e loop de referência
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		var projeto = projetoService.getById(idProjeto);
		var projetoExportDTO = new ProjetoExportDTO();
		
		projetoExportDTO.setId(projeto.getId());
		projetoExportDTO.setNome(projeto.getNome());
		projetoExportDTO.setDescricao(projeto.getDescricao());
		projetoExportDTO.setQuantidadeDiasSprint(projeto.getQuantidadeDiasSprint());
		projetoExportDTO.setDuracaoMinutosDaily(projeto.getDuracaoMinutosDaily());
		projetoExportDTO.setDuracaoMinutosPlanning(projeto.getDuracaoMinutosPlanning());
		projetoExportDTO.setDuracaoMinutosReview(projeto.getDuracaoMinutosReview());
		projetoExportDTO.setDuracaoMinutosRetrospective(projeto.getDuracaoMinutosRetrospective());
		projetoExportDTO.setSituacao(projeto.getSituacao());
		projetoExportDTO.setEventoFinalizacao(projeto.getEventoFinalizacao());
		projetoExportDTO.setSprints(projeto.getSprints().stream().map(sprint -> modelMapper.map(sprint, SprintExportDTO.class)).collect(Collectors.toList()));
		projetoExportDTO.setBacklog(projeto.getBacklog().stream().map(backlog -> modelMapper.map(backlog, ItemBacklogProjetoDTO.class)).collect(Collectors.toList()));
		projetoExportDTO.setTime(projeto.getTime().stream().map(time -> modelMapper.map(time, ProjetoUsuarioDTO.class)).collect(Collectors.toList()));
		
		return projetoExportDTO;
	}

}
