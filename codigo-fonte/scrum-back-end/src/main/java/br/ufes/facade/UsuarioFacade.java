package br.ufes.facade;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.ufes.dto.UsuarioDTO;
import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.UsuarioUpdateSenhaAdminDTO;
import br.ufes.dto.UsuarioUpdateSenhaDTO;
import br.ufes.dto.UsuarioUpsertDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.entity.Usuario;
import br.ufes.exception.BusinessException;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;

@Component
public class UsuarioFacade {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ModelMapper modelMapper;

	public UsuarioResponseDTO cadastrarUsuario(UsuarioUpsertDTO usuarioDTO) throws Exception {
		if (usuarioService.existsByNomeUsuario(usuarioDTO.getNomeUsuario())) {
			throw new BusinessException("Nome de usuário já cadastrado");
		}

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
		var usuario = modelMapper.map(usuarioDTO, Usuario.class);
		usuario.setSenha(senhaCriptografada);
		usuario.setAtivo(true);

		usuario = usuarioService.insert(usuario);

		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public UsuarioResponseDTO getById(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);
		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public ResponseSearch<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO) throws Exception {
		var responseSearch = new ResponseSearch<UsuarioResponseDTO>();
		
		List<UsuarioResponseDTO> listPage = usuarioService.search(usuarioFilterDTO);
		Long total = usuarioService.searchCount(usuarioFilterDTO);
		
		responseSearch.setListPage(listPage);
		responseSearch.setTotal(total);
		return responseSearch;
	}

	public void inativarUsuario(Long idUsuario) throws Exception {
	}

	public void atualizarSenhaUsuario(UsuarioUpdateSenhaDTO udpateSenhaDTO) throws Exception {
	}

	public void atualizarSenhaUsuarioByAdmin(UsuarioUpdateSenhaAdminDTO udpateSenhaDTO) throws Exception {
	}

	public UsuarioDTO atualizarUsuario(Long idUsuario, UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		var usuarioDTO = modelMapper.map(usuarioUpdateDTO, UsuarioDTO.class);
		usuarioDTO.setId(idUsuario);
		return usuarioDTO;
	}

}
