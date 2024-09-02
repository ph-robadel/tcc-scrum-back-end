package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoUpdateDTO;
import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.exception.BusinessException;
import br.ufes.exception.RequestArgumentException;

@Component
public class ItemBacklogSprintValidate {

	public void validateSave(ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) throws Exception {
		List<String> erros = new ArrayList<>();

		try {
			ObjectUtils.isEmpty(itemBacklogSprintUpsertDTO.getSituacao());
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
