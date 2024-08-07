package br.ufes.facade;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.services.ProjetoService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;

@Component
public class ProjetoFacade {

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ModelMapper modelMapper;

	public ResponseSearch<ProjetoDTO> search(ProjetoFilterDTO filterDTO) throws Exception {
		var projetoMock = projetoService.getMock();
		
		return new ResponseSearch<>(List.of(projetoMock), 1l);
	}

	public ProjetoDTO getById(Long idProjeto) throws Exception {
		return projetoService.getMock();
	}

	public void inativarProjeto(Long idProjeto) throws Exception {
	}

	public ProjetoDTO cadastrarProjeto(ProjetoUpsertDTO projetoInsertDTO) throws Exception {		
		var projetoDTO = modelMapper.map(projetoInsertDTO, ProjetoDTO.class);
		projetoDTO.setId(1L);
		return projetoDTO;
	}

	public ProjetoDTO atualizarProjeto(Long idProjeto, ProjetoUpsertDTO projetoUpdateDTO) throws Exception {
		var projetoDTO = modelMapper.map(projetoUpdateDTO, ProjetoDTO.class);
		projetoDTO.setId(idProjeto);
		return projetoDTO;
	}

	public ResponseSearch<UsuarioResponseDTO> searchProjetoUsuario(Long idProjeto,
			ProjetoUsuarioFilterDTO projetoUsuarioFiltroDTO) throws Exception {
		
		var usuarioMock = usuarioService.getResponseMock();
		
		return new ResponseSearch<>(List.of(usuarioMock), 1l);
	}

	public void cadastrarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
	}

	public void inativarProjetoUsuario(Long idProjeto, Long idUsuario) throws Exception {
	}

}
