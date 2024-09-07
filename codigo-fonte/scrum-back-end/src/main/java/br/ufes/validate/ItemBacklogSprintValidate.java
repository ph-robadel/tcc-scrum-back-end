package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.entity.Sprint;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.exception.RequestArgumentException;

@Component
public class ItemBacklogSprintValidate {

	public void validateSave(ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO, Sprint sprint) throws Exception {
		List<String> erros = new ArrayList<>();
		var situacaoSprint = sprint.getSituacao();

		if (!SituacaoSprintEnum.EM_PLANEJAMENTO.equals(situacaoSprint)
				&& !SituacaoSprintEnum.EM_ANDAMENTO.equals(situacaoSprint)) {
			
			throw new BusinessException("Não é possível adicionar novos itens no backlog de sprint com situacao " + situacaoSprint.getSituacao());
		}
		try {
			ObjectUtils.isEmpty(itemBacklogSprintUpsertDTO.getSituacao());
		} catch (RequestArgumentException e) {
			erros.add(e.getMessage());
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}
}
