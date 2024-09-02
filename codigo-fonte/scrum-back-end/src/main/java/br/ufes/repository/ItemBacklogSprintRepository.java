package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.ItemBacklogSprintBasicDTO;
import br.ufes.dto.filter.ItemBacklogSprintFilterDTO;
import br.ufes.entity.ItemBacklogSprint;

@Repository
public interface ItemBacklogSprintRepository extends JpaRepository<ItemBacklogSprint, Long> {

	List<ItemBacklogSprintBasicDTO> search(ItemBacklogSprintFilterDTO filterDTO);

	Long searchCount(ItemBacklogSprintFilterDTO filterDTO);

	Long obterNumeroPrioridadeNovoItem(Long idSprint);

	void diminuirPrioridadeItem(Long idItemBacklogSprint, Long antigaPrioridade, Long novaPrioridade);

	void aumentarPrioridadeItem(Long idItemBacklogSprint, Long antigaPrioridade, Long novaPrioridade);

	void repriorizarDeleteItem(Long idItemBacklogSprint);

}
