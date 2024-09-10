package br.ufes.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.entity.ItemBacklogSprint;
import br.ufes.entity.Sprint;
import br.ufes.entity.Usuario;
import br.ufes.enums.SituacaoItemSprintEnum;
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
	
	public List<ItemBacklogSprint> gerarItensBacklogSprintByPlanning(Sprint sprint, Usuario usuarioAutenticado) {
		var dataHoraAtual = LocalDateTime.now();
		List<ItemBacklogSprint> itens = new ArrayList<>();

		for (var item : sprint.getPlanning().getItensSelecionados()) {
			var itemBacklogSprint = new ItemBacklogSprint();
			var prioridadeNovoItem = obterNumeroPrioridadeNovoItem(sprint.getId());

			itemBacklogSprint.setPrioridade(prioridadeNovoItem);
			itemBacklogSprint.setDescricao(item.getDescricao());
			itemBacklogSprint.setHorasEstimadas(item.getHorasEstimadas());
			itemBacklogSprint.setHorasRealizadas(BigDecimal.ZERO);
			itemBacklogSprint.setResponsavelRealizacao(item.getResponsavelRealizacao());
			itemBacklogSprint.setResponsavelInclusao(usuarioAutenticado);
			itemBacklogSprint.setSprint(sprint);
			itemBacklogSprint.setItemBacklogProjeto(item.getItemBacklogProjeto());
			itemBacklogSprint.setDataInclusao(dataHoraAtual);
			itemBacklogSprint.setSituacao(SituacaoItemSprintEnum.A_FAZER);
			itens.add(save(itemBacklogSprint));
		}
		
		return itens;
	}

	public void repriorizarItemBacklogSprint(ItemBacklogSprint itemBacklogSprint, Long valorPrioridade) {
		var sprint = itemBacklogSprint.getSprint();
		var valorMaximoPrioridade = this.obterNumeroPrioridadeNovoItem(sprint.getId());
		Long antigaPrioridade = itemBacklogSprint.getPrioridade();
		Long novaPrioridade = null;
		if (valorPrioridade <= 0) {
			novaPrioridade = 1L;
		} else if (valorPrioridade < valorMaximoPrioridade) {
			novaPrioridade = valorPrioridade;
		} else {
			novaPrioridade = valorMaximoPrioridade - 1;
		}

		if (antigaPrioridade > novaPrioridade) {
			itemBacklogSprintRepository.aumentarPrioridadeItem(itemBacklogSprint.getId(), antigaPrioridade,
					novaPrioridade);
		} else {
			itemBacklogSprintRepository.diminuirPrioridadeItem(itemBacklogSprint.getId(), antigaPrioridade,
					novaPrioridade);
		}
		
	}
	
}
