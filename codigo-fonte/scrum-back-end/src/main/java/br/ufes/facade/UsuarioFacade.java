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
import br.ufes.services.UsuarioService;
import util.ResponseSearch;

@Component
public class UsuarioFacade {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ModelMapper modelMapper;

	public UsuarioResponseDTO cadastrarUsuario(UsuarioUpsertDTO usuarioDTO) throws Exception {
		if (usuarioService.findByNomeUsuario(usuarioDTO.getNomeUsuario()) != null) {
			throw new RuntimeException("O nome de usuário já cadastrado");
		}

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
		var usuario = modelMapper.map(usuarioDTO, Usuario.class);
		usuario.setSenha(senhaCriptografada);
		usuario.setAtivo(true);

		usuario = usuarioService.insert(usuario);
		
		throw new Exception();
//		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public UsuarioResponseDTO getById(Long idUsuario) throws Exception {
		return usuarioService.getResponseMock();
	}

	public ResponseSearch<UsuarioResponseDTO> search(UsuarioFilterDTO filterDTO) throws Exception {
		var responseSearch = new ResponseSearch<UsuarioResponseDTO>();
		var usuarioMock = usuarioService.getResponseMock();
		responseSearch.setLista(List.of(usuarioMock));
		responseSearch.setTotal(1L);
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
