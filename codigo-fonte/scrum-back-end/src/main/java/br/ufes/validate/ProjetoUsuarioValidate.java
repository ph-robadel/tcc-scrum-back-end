package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.entity.Projeto;
import br.ufes.entity.Usuario;
import br.ufes.exception.BusinessException;
import br.ufes.services.ProjetoUsuarioService;

@Component
public class ProjetoUsuarioValidate {

	@Autowired
	private ProjetoUsuarioService projetoUsuarioService;

	public void validateSave(Projeto projeto, Usuario usuario) throws Exception {
		List<String> erros = new ArrayList<>();
		boolean projetoEncontrado = ObjectUtils.isEmpty(projeto);
		boolean usuarioEncontrado = ObjectUtils.isEmpty(usuario);
		
		if (projetoEncontrado) {
			erros.add("Projeto não encontado");
		} else if (!usuario.isAtivo()) {
			erros.add("O usuário está inativo");
		}

		if (usuarioEncontrado) {
			erros.add("Usuário não encontado");
		} else if (!projeto.isAtivo()) {
			erros.add("O projeto está inativo");
		}

		if (projetoEncontrado && usuarioEncontrado && !ObjectUtils.isEmpty(projetoUsuarioService.getByIdProjetoAndIdUsuario(projeto.getId(), usuario.getId()))) {
			erros.add("O usuário já foi incluído ao projeto");
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

}
