package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoInsertDTO;
import br.ufes.dto.ItemBacklogProjetoUpdateDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.exception.BusinessException;
import br.ufes.exception.RequestArgumentException;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.UsuarioService;

@Component
public class ItemBacklogProjetoValidate {
	
	@Autowired
	public ItemBacklogProjetoService itemBacklogProjetoService;
	
	@Autowired
	public UsuarioService usuarioService;

	public void validateSave(ItemBacklogProjetoInsertDTO itemBacklogProjetoUpsertDTO) throws Exception {
		List<String> erros = new ArrayList<>();

		if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getTitulo())) {
			erros.add("Informe o título");
		}

		if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getDescricao())) {
			erros.add("Informe a descrição");
		}

		try {
			if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getCategoria())) {
				erros.add("Informe a categoria");
			}
		} catch (RequestArgumentException e) {
			erros.add(e.getMessage());
		}

		try {
			ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getSituacao());
		} catch (RequestArgumentException e) {
			erros.add(e.getMessage());
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validateSave(ItemBacklogProjetoUpdateDTO itemBacklogProjetoUpsertDTO) throws Exception {
		List<String> erros = new ArrayList<>();

		if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getTitulo())) {
			erros.add("Informe o título");
		}

		if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getDescricao())) {
			erros.add("Informe a descrição");
		}

		if (ObjectUtils.isEmpty(itemBacklogProjetoUpsertDTO.getSituacao())) {
			erros.add("Informe a situação");
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}
	
	public void validateDeleteItemBacklogProjeto(ItemBacklogProjeto itemBacklogProjeto) {
		if (itemBacklogProjetoService.possuiItemSprintAssociado(itemBacklogProjeto.getId())) {
			throw new BusinessException("Há itens no backlog de sprint associados a esse item");
		}

		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		if (!PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())
				&& usuarioAutenticado.equals(itemBacklogProjeto.getAutor())) {

			throw new BusinessException("O usuário '" + usuarioAutenticado.getNomeUsuario()
					+ "' possui permissão para remover apenas os itens de backlog de projeto de sua autoria");
		}
	}
}
