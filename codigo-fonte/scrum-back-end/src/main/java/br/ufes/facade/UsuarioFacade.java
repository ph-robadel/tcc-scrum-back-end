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
import br.ufes.validate.UsuarioValidate;

@Component
public class UsuarioFacade {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioValidate usuarioValidate;

	@Autowired
	private ModelMapper modelMapper;

	public UsuarioResponseDTO cadastrarUsuario(UsuarioUpsertDTO usuarioDTO) throws Exception {
		usuarioValidate.validateSave(usuarioDTO, null);

		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioDTO.getSenha());
		var usuario = modelMapper.map(usuarioDTO, Usuario.class);
		usuario.setSenha(senhaCriptografada);
		usuario.setAtivo(true);

		usuario = usuarioService.save(usuario);

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
		return usuarioService.search(usuarioFilterDTO);
	}

	public void inativarUsuarioAutenticado() throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		usuarioAutenticado.setAtivo(false);
		usuarioService.save(usuarioAutenticado);
	}

	public void inativarUsuario(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);

		if (!usuario.isAtivo()) {
			throw new BusinessException("Usuário já está inativo");
		}

		usuario.setAtivo(false);
		usuarioService.save(usuario);
	}

	public void reativar(Long idUsuario) throws Exception {
		var usuario = usuarioService.getById(idUsuario);

		if (usuario.isAtivo()) {
			throw new BusinessException("Usuário já está ativo");
		}

		usuario.setAtivo(true);
		usuarioService.save(usuario);
	}

	public void atualizarSenhaUsuario(UsuarioUpdateSenhaDTO udpateSenhaDTO) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		var isMatche = new BCryptPasswordEncoder().matches(udpateSenhaDTO.getSenhaAtual(),
				usuarioAutenticado.getPassword());
		if (!isMatche) {
			throw new BusinessException("Senha atual inválida");
		}

		atualizarSenha(udpateSenhaDTO.getNovaSenha(), usuarioAutenticado);
	}

	public void atualizarSenhaUsuarioByAdmin(Long idUsuario, UsuarioUpdateSenhaAdminDTO udpateSenhaDTO)
			throws Exception {
		var usuario = usuarioService.getById(idUsuario);
		atualizarSenha(udpateSenhaDTO.getNovaSenha(), usuario);
	}

	public UsuarioDTO atualizarUsuarioAutenticado(UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		return updateUsuario(usuarioUpdateDTO, usuarioAutenticado.getId());
	}

	public UsuarioDTO atualizarUsuario(Long idUsuario, UsuarioUpsertDTO usuarioUpdateDTO) throws Exception {
		return updateUsuario(usuarioUpdateDTO, idUsuario);
	}

	private UsuarioDTO updateUsuario(UsuarioUpsertDTO usuarioUpdateDTO, Long idUsuario) throws Exception {
		usuarioValidate.validateSave(usuarioUpdateDTO, idUsuario);

		var usuario = usuarioService.getById(idUsuario);
		usuario.atualizarAtributos(usuarioUpdateDTO);
		usuarioService.save(usuario);

		return modelMapper.map(usuario, UsuarioDTO.class);
	}

	private void atualizarSenha(String novaSenha, Usuario usuario) throws Exception {
		usuarioValidate.validateSenha(novaSenha);
		String senhaCriptografada = new BCryptPasswordEncoder().encode(novaSenha);
		usuario.setSenha(senhaCriptografada);
		usuarioService.save(usuario);
	}

}
