package br.ufes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.entity.Sprint;
import br.ufes.repository.ItemBacklogSprintRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemBacklogSprintService {
	
	@Autowired
	private ItemBacklogSprintRepository itemBacklogSprintRepository;

	public void criarItensNovaSprint(Sprint sprint) {
	}

	public ResponseSearch<ItemBacklogSprintBasicDTO> search(ItemBacklogSprintFilterDTO filterDTO) {
		var listPage = itemBacklogSprintRepository.search(filterDTO);
		var total = itemBacklogSprintRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public Long obterNumeroPrioridadeNovoItem(Long idSprint) {
		return itemBacklogSprintRepository.obterNumeroPrioridadeNovoItem(idSprint);
	}
	
	public ItemBacklogSprint save(ItemBacklogSprint itemBacklogSprint) {
		return itemBacklogSprintRepository.save(itemBacklogSprint);
	}

	public ItemBacklogSprint getById(Long idItemBacklogSprint) {
		if (idItemBacklogSprint == null) {
			return null;
		}

		var itemBacklogProjeto = itemBacklogSprintRepository.findById(idItemBacklogSprint)
				.orElseThrow(() -> new EntityNotFoundException("Item backlog sprint não encontrado"));

		return itemBacklogProjeto;
	}

	public void delete(Long idItemBacklogSprint) {
		itemBacklogSprintRepository.repriorizarDeleteItem(idItemBacklogSprint);
		itemBacklogSprintRepository.deleteById(idItemBacklogSprint);
	}
}
