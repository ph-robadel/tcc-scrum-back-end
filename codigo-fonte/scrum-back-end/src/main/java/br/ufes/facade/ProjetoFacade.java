package br.ufes.facade;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.entity.Projeto;
import br.ufes.entity.ProjetoUsuario;
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
		projeto.setAtivo(true);

		projeto = projetoService.save(projeto);
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public ResponseSearch<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO) throws Exception {
		var usuario = usuarioService.getUsuarioAutenticado();
		filterDTO.setIdUsuario(usuario.getId());

		List<ProjetoBasicDTO> listPage = projetoService.search(filterDTO);
		Long total = projetoService.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public ProjetoDTO getById(Long idProjeto) throws Exception {

		var projeto = projetoService.getById(idProjeto);
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public void inativarProjeto(Long idProjeto) throws Exception {
		var projeto = projetoService.getById(idProjeto);

		if (!projeto.isAtivo()) {
			throw new BusinessException("Projeto já está inativo");
		}

		projeto.setAtivo(false);
		projetoService.save(projeto);
	}

	public void ativarProjeto(Long idProjeto) throws Exception {
		var projeto = projetoService.getById(idProjeto);

		if (projeto.isAtivo()) {
			throw new BusinessException("Projeto já está ativo");
		}

		projeto.setAtivo(true);
		projetoService.save(projeto);
	}

	public ProjetoDTO atualizarProjeto(Long idProjeto, ProjetoUpsertDTO projetoInsertDTO) throws Exception {
		projetoValidate.validateSave(projetoInsertDTO);

		var projeto = projetoService.getById(idProjeto);
		projeto.atualizarAtributos(projetoInsertDTO);

		projeto = projetoService.save(projeto);
		return modelMapper.map(projeto, ProjetoDTO.class);
	}

	public ResponseSearch<UsuarioResponseDTO> searchProjetoUsuario(Long idProjeto, ProjetoUsuarioFilterDTO filterDTO)
			throws Exception {

		var projeto = projetoService.getById(filterDTO.getIdProjeto());
		if (!projeto.isAtivo()) {
			throw new BusinessException("Projeto inativo");
		}

		filterDTO.setIdProjeto(idProjeto);

		List<UsuarioResponseDTO> listPage = projetoUsuarioService.searchProjetoUsuario(filterDTO);
		Long total = projetoUsuarioService.searchProjetoUsuarioCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public void cadastrarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
		var projeto = projetoService.getById(idProjeto);
		var usuario = usuarioService.getById(idUsuario);

		projetoUsuarioValidate.validateSave(projeto, usuario);

		var projetoUsuario = new ProjetoUsuario(projeto, usuario);
		projetoUsuarioService.save(projetoUsuario);

	}

	public void inativarProjetoUsuario(Long idProjeto, Long idUsuario) {
		var projetoUsuario = projetoUsuarioService.getByIdProjetoAndIdUsuario(idProjeto, idUsuario);

		if (ObjectUtils.isEmpty(projetoUsuario)) {
			throw new BusinessException("O usuário não está cadastrado ao projeto informado");
		} else if (!projetoUsuario.isAtivo()) {
			throw new BusinessException("O usuário já está inativo neste projeto");
		}

		projetoUsuario.setAtivo(false);
		projetoUsuarioService.save(projetoUsuario);
	}

	public void reativarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
		var projetoUsuario = projetoUsuarioService.getByIdProjetoAndIdUsuario(idProjeto, idUsuario);

		if (ObjectUtils.isEmpty(projetoUsuario)) {
			throw new BusinessException("O usuário não está cadastrado ao projeto informado");
		} else if (!projetoUsuario.isAtivo()) {
			throw new BusinessException("O usuário já está inativo neste projeto");
		}

		projetoUsuario.setAtivo(true);
		projetoUsuarioService.save(projetoUsuario);
	}

}
