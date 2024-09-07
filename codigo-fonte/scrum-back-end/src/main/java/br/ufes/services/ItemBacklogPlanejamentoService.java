package br.ufes.services;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.entity.ItemBacklogPlanejamento;
import br.ufes.exception.BusinessException;
import br.ufes.repository.ItemBacklogPlanejamentoRepository;
import br.ufes.util.ResponseSearch;

@Service
public class ItemBacklogPlanejamentoService {

	@Autowired
	private ItemBacklogPlanejamentoRepository itemBacklogPlanejamentoRepository;

	public ResponseSearch<ItemBacklogProjetoSimpleDTO> search(ItemBacklogPlanejamentoFilterDTO filterDTO) {
		var listPage = itemBacklogPlanejamentoRepository.search(filterDTO);
		var total = itemBacklogPlanejamentoRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public ItemBacklogPlanejamento save(ItemBacklogPlanejamento itemBacklogPlanejamento) {
		return itemBacklogPlanejamentoRepository.save(itemBacklogPlanejamento);
	}

	public ItemBacklogPlanejamento getByIdSprintAndIdItemBacklogProjeto(Long idSprint, Long idItemBacklogSprint) {
		var  itemBacklogPlanejamento = findByIds(idSprint, idItemBacklogSprint);
		
		if(ObjectUtils.isEmpty(itemBacklogPlanejamento)) {
			throw new BusinessException("Item backlog planejamento não encontrado");
		}
		
		return itemBacklogPlanejamento;
	}
	
	public boolean possuiItemBacklogPlanejamento(Long idSprint, Long idItemBacklogSprint) {
		var  itemBacklogPlanejamento = findByIds(idSprint, idItemBacklogSprint);
		
		return !ObjectUtils.isEmpty(itemBacklogPlanejamento);
	}

	private ItemBacklogPlanejamento findByIds(Long idSprint, Long idItemBacklogSprint) {
		if (idItemBacklogSprint == null || idSprint == null) {
			return null;
		}

		var itemBacklogPlanejamento = itemBacklogPlanejamentoRepository.findBySprintIdAndItemBacklogProjetoId(idSprint,
				idItemBacklogSprint);

		return ObjectUtils.isEmpty(itemBacklogPlanejamento) ? null : itemBacklogPlanejamento.get(0);
	}

	public void delete(Long idSprint, Long idItemBacklogSprint) {
		var itemPlanejamento = getByIdSprintAndIdItemBacklogProjeto(idSprint, idItemBacklogSprint);
		itemBacklogPlanejamentoRepository.deleteById(itemPlanejamento.getId());
	}
}
