package br.ufes.validate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.entity.Sprint;
import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.enums.SituacaoProjetoEnum;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ItemBacklogPlanejamentoService;

@Component
public class ItemBacklogPlanejamentoValidate {

	@Autowired
	private ItemBacklogPlanejamentoService itemBacklogPlanejamentoService;

	public void validateSave(Sprint sprint, ItemBacklogProjeto itemBacklogProjeto) throws BusinessException {
		List<String> erros = new ArrayList<>();
		var situacaoSprint = sprint.getSituacao();
		var situacaoItem = itemBacklogProjeto.getSituacao();
		var situacaoProjeto = sprint.getProjeto().getSituacao();

		if (!SituacaoProjetoEnum.EM_ANDAMENTO.equals(situacaoProjeto)) {
			throw new BusinessException(
					"Não é possível adicionar novos itens no backlog de planejamento da sprint de um projeto com situacao "
							+ situacaoProjeto.getSituacao());
		}

		if (!SituacaoSprintEnum.CRIADA.equals(situacaoSprint)) {
			erros.add("Não é possível adicionar novos itens no backlog de planejamento com a sprint tendo a situacao "
					+ situacaoSprint.getSituacao());
		}

		if (!SituacaoItemProjetoEnum.APROVADO.equals(situacaoItem)
				&& !SituacaoItemProjetoEnum.EM_DESENVOLVIMENTO.equals(situacaoItem)) {
			erros.add(
					"O item de backlog do projeto possui a situacao "
							+ situacaoItem.getSituacao());
		}

		if (itemBacklogPlanejamentoService.possuiItemBacklogPlanejamento(sprint.getId(), itemBacklogProjeto.getId())) {
			erros.add("O item já foi adicionado ao backlog de planejamento da sprint");
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validateDelete(Sprint sprint, ItemBacklogProjeto itemBacklogProjeto) throws BusinessException {
		List<String> erros = new ArrayList<>();
		var situacaoSprint = sprint.getSituacao();
		var situacaoItem = itemBacklogProjeto.getSituacao();
		var situacaoProjeto = sprint.getProjeto().getSituacao();

		if (!SituacaoProjetoEnum.EM_ANDAMENTO.equals(situacaoProjeto)) {
			throw new BusinessException(
					"Não é possível remover itens do backlog de planejamento da sprint de um projeto com situacao "
							+ situacaoProjeto.getSituacao());
		}

		if (!SituacaoSprintEnum.CRIADA.equals(situacaoSprint)) {
			erros.add("Não é possível remover itens do backlog de planejamento da sprint com situacao "
					+ situacaoSprint.getSituacao());
		}

		if (!SituacaoItemProjetoEnum.APROVADO.equals(situacaoItem)
				&& !SituacaoItemProjetoEnum.EM_DESENVOLVIMENTO.equals(situacaoItem)) {
			erros.add(
					"Não é possível remover itens do backlog de planejamento da sprint com o item de backlog do projeto com situacao "
							+ situacaoItem.getSituacao());
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}
}
