package br.ufes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.repository.ItemBacklogProjetoRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemBacklogProjetoService {

	@Autowired
	private ItemBacklogProjetoRepository itemBacklogProjetoRepository;

	public ItemBacklogProjeto getById(Long idItemBacklogProjeto) {
		if (idItemBacklogProjeto == null) {
			return null;
		}

		var itemBacklogProjeto = itemBacklogProjetoRepository.findById(idItemBacklogProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Item backlog projeto não encontrado"));

		return itemBacklogProjeto;
	}

	public Long obterCodigoNovoItem(Long idProjeto, CategoriaItemProjetoEnum categoria) {
		return itemBacklogProjetoRepository.obterCodigoNovoItem(idProjeto, categoria);
	}

	public Long obterNumeroPrioridadeNovoItem(Long idProjeto) {
		return itemBacklogProjetoRepository.obterNumeroPrioridadeNovoItem(idProjeto);
	}

	public ItemBacklogProjeto save(ItemBacklogProjeto itemBacklogProjeto) {
		return itemBacklogProjetoRepository.save(itemBacklogProjeto);
	}

	public ResponseSearch<ItemBacklogProjetoBasicDTO> search(ItemBacklogProjetoFilterDTO filterDTO) {
		var listPage = itemBacklogProjetoRepository.search(filterDTO);
		var total = itemBacklogProjetoRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

	public void repriorizarItemBacklogProjeto(ItemBacklogProjeto itemBacklogProjeto, Long valorNovaPrioridade) {
		var idProjeto = itemBacklogProjeto.getProjeto().getId();
		var valorMaximoPrioridade = this.obterNumeroPrioridadeNovoItem(idProjeto);
		Long antigaPrioridade = itemBacklogProjeto.getPrioridade();
		Long novaPrioridade = null;
		if (valorNovaPrioridade <= 0) {
			novaPrioridade = 1L;
		} else if (valorNovaPrioridade < valorMaximoPrioridade) {
			novaPrioridade = valorNovaPrioridade;
		} else {
			novaPrioridade = valorMaximoPrioridade - 1;
		}

		if (antigaPrioridade > novaPrioridade) {
			itemBacklogProjetoRepository.aumentarPrioridadeItem(itemBacklogProjeto.getId(), antigaPrioridade,
					novaPrioridade);
		} else {
			itemBacklogProjetoRepository.diminuirPrioridadeItem(itemBacklogProjeto.getId(), antigaPrioridade,
					novaPrioridade);
		}

	}

	public void remover(Long itemBacklogProjeto) {
		itemBacklogProjetoRepository.repriorizarDeleteItem(itemBacklogProjeto);
		itemBacklogProjetoRepository.deleteById(itemBacklogProjeto);
	}

	public boolean possuiItemSprintAssociado(Long idItemBacklogProjeto) {
		return itemBacklogProjetoRepository.possuiItemSprintAssociado(idItemBacklogProjeto);
	}

}
