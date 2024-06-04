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
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.entity.Usuario;
import br.ufes.services.UsuarioService;
import util.ResponseSearch;

@Component
public class UsuarioFacade {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UsuarioService usuarioService;

	public UsuarioResponseDTO cadastrarUsuario(UsuarioDTO usuarioDTO) throws Exception {
		if (usuarioService.findByNomeUsuario(usuarioDTO.getNomeUsuario()) != null) {
			throw new RuntimeException("O nome de usuário já cadastrado");
		}

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
		var usuario = modelMapper.map(usuarioDTO, Usuario.class);
		usuario.setSenha(senhaCriptografada);

		usuario = usuarioService.insert(usuario);
		return modelMapper.map(usuario, UsuarioResponseDTO.class);
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

	public void atualizarSenhaUsuario(UsuarioUpdateSenhaDTO udpateSenhaDto) throws Exception {
	}

	public void atualizarSenhaUsuarioByAdmin(UsuarioUpdateSenhaAdminDTO udpateSenhaDto) throws Exception {
	}

}
