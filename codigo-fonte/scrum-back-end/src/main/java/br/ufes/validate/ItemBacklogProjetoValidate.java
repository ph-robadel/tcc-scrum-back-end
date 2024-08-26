package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoInsertDTO;
import br.ufes.dto.ItemBacklogProjetoUpdateDTO;
import br.ufes.exception.BusinessException;
import br.ufes.exception.RequestArgumentException;

@Component
public class ItemBacklogProjetoValidate {

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
}
