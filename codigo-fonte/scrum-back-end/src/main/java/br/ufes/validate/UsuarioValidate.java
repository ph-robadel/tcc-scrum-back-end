package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.UsuarioUpsertDTO;
import br.ufes.services.UsuarioService;

@Component
public class UsuarioValidate {

	@Autowired
	private UsuarioService usuarioService;

	public void validateUpsert(UsuarioUpsertDTO usuarioUpsertDTO, Long idUsuario) throws Exception {
		List<String> erros = new ArrayList<>();
		boolean isUpdate = !ObjectUtils.isEmpty(idUsuario);
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
		}

		if (ObjectUtils.isEmpty(usuarioUpsertDTO.getPerfil())) {
			erros.add("Informe o perfil");
		}
	}
}
