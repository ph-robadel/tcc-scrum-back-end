package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.entity.Projeto;
import br.ufes.entity.Usuario;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ProjetoUsuarioService;
import br.ufes.services.UsuarioService;

@Component
public class ProjetoUsuarioValidate {

	@Autowired
	private ProjetoUsuarioService projetoUsuarioService;
	
	@Autowired
	private UsuarioService usuarioService;

	public void validateSave(Projeto projeto, Usuario usuario) throws Exception {
		List<String> erros = new ArrayList<>();
		boolean projetoEncontrado = !ObjectUtils.isEmpty(projeto);
		boolean usuarioEncontrado = !ObjectUtils.isEmpty(usuario);
		
		if (!projetoEncontrado) {
			erros.add("Projeto não encontado");
		} else if (!usuario.isAtivo()) {
			erros.add("O usuário está inativo");
		}

		if (!usuarioEncontrado) {
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
	
	public void validarAcessoUsuarioAutenticadoAoProjeto(Long idProjeto) throws BusinessException {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		validateAcessoUsuarioAoProjeto(idProjeto, usuarioAutenticado);
	}
	
	public void validarAcessoAdminOuUsuarioAutenticadoAoProjeto(Long idProjeto) throws BusinessException {
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (!PerfilUsuarioEnum.ADMINISTRADOR.equals(usuarioAutenticado.getPerfil())) {
			validateAcessoUsuarioAoProjeto(idProjeto, usuarioAutenticado);
		}
	}

	private void validateAcessoUsuarioAoProjeto(Long idProjeto, Usuario usuarioAutenticado) {
		var projetoUsuario = projetoUsuarioService.getByIdProjetoAndIdUsuario(idProjeto, usuarioAutenticado.getId());

		if (ObjectUtils.isEmpty(projetoUsuario)) {
			throw new BusinessException("Usuário autenticado não possui acesso ao projeto requisitado");
		}
	}

}
