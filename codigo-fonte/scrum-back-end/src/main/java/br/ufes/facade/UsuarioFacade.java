package br.ufes.facade;

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

		usuario = usuarioService.upsert(usuario);

		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public UsuarioResponseDTO getById(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);
		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public UsuarioResponseDTO getUsuarioAutenticado() throws Exception {
		var usuario = usuarioService.getUsuarioAutenticado();
		return modelMapper.map(usuario, UsuarioResponseDTO.class);
	}

	public ResponseSearch<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO) {
		var listPage = usuarioService.search(usuarioFilterDTO);
		var total = usuarioService.searchCount(usuarioFilterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public void inativarUsuarioAutenticado() throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		usuarioAutenticado.setAtivo(false);
		usuarioService.upsert(usuarioAutenticado);
	}

	public void inativarUsuario(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);

		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new BusinessException("Usuário já está inativo");
		}

		usuario.setAtivo(false);
		usuarioService.upsert(usuario);
	}

	public void reativar(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);

		if (Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new BusinessException("Usuário já está ativo");
		}

		usuario.setAtivo(true);
		usuarioService.upsert(usuario);
	}

	public void reativarUsuario(Long idUsuario) throws Exception {
	}

	public void atualizarSenhaUsuario(UsuarioUpdateSenhaDTO udpateSenhaDTO) throws Exception {
	}

	public void atualizarSenhaUsuarioByAdmin(Long idUsuario, UsuarioUpdateSenhaAdminDTO udpateSenhaDTO) throws Exception {
	}

	public UsuarioDTO atualizarUsuarioAutenticado(UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		return updateUsuario(usuarioUpdateDTO, usuarioAutenticado);
	}
	
	public UsuarioDTO atualizarUsuario(Long idUsuario, UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		var usuario = usuarioService.getById(idUsuario);
		return updateUsuario(usuarioUpdateDTO, usuario);
	}

	private UsuarioDTO updateUsuario(UsuarioUpsertDTO usuarioUpdateDTO, Usuario usuario) {
		// TODO: validar update
		usuario.atualizarAtributos(usuarioUpdateDTO);
		usuarioService.upsert(usuario);
		
		return modelMapper.map(usuario, UsuarioDTO.class);
	}

}
