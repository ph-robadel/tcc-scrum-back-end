package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.UsuarioUpsertDTO;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.exception.AcessoUsuarioException;
import br.ufes.exception.BusinessException;
import br.ufes.exception.RequestArgumentException;
import br.ufes.services.UsuarioService;

@Component
public class UsuarioValidate {

	@Autowired
	private UsuarioService usuarioService;

	public void validateSave(UsuarioUpsertDTO usuarioUpsertDTO, Long idUsuario) throws Exception {
		List<String> erros = new ArrayList<>();
		boolean isUpdate = !ObjectUtils.isEmpty(idUsuario);
		boolean existeUsuariosSalvos = usuarioService.existeUsuariosSalvos();

		if (existeUsuariosSalvos) {
			var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
			if (ObjectUtils.isEmpty(usuarioAutenticado)) {
				throw new AcessoUsuarioException("");
			} else if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioAutenticado.getPerfil())) {
				erros.add("Apenas usuários de perfil Administrador possuem permissão para adicionar novos usuários");
			}
		} else if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioUpsertDTO.getPerfil())) {
			erros.add("O primeiro usuário deve ser do perfil administrador");
		}
		if (ObjectUtils.isEmpty(usuarioUpsertDTO.getNomeCompleto())) {
			erros.add("Informe o nome completo");
		}

		if (ObjectUtils.isEmpty(usuarioUpsertDTO.getNomeUsuario())) {
			erros.add("Informe o nome de usuário");
		} else if (!usuarioService.isNomeUsuarioDisponível(usuarioUpsertDTO.getNomeUsuario(), idUsuario)) {
			erros.add("Nome de usuário indisponível");
		}

		if (!isUpdate && ObjectUtils.isEmpty(usuarioUpsertDTO.getSenha())) {
			erros.add("Informe a senha do usuário");
		} else {
			erros.addAll(getListErrosSenha(usuarioUpsertDTO.getSenha()));
		}

		try {
			if (ObjectUtils.isEmpty(usuarioUpsertDTO.getPerfil())) {
				erros.add("Informe o perfil");
			}
		} catch (RequestArgumentException ex) {
			erros.add(ex.getMessage());
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validateSenha(String senha) throws Exception {
		var erros = getListErrosSenha(senha);

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	private List<String> getListErrosSenha(String senha) throws Exception {
		List<String> erros = new ArrayList<>();
		var senhaVazia = ObjectUtils.isEmpty(senha);

		if (senhaVazia || senha.length() < 6) {
			erros.add("A senha deve ter 6 ou mais caracteres");
		}

		if (senhaVazia || !senha.matches(".*\\d.*")) {
			erros.add("A senha deve conter ao menos um número");
		}

		if (senhaVazia || !senha.matches(".*[a-zA-Z].*")) {
			erros.add("A senha deve conter ao menos uma letra");
		}

		return erros;
	}
}
