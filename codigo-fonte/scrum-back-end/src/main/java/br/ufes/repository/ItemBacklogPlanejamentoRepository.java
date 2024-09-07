package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import br.ufes.entity.ItemBacklogPlanejamento;

@Repository
public interface ItemBacklogPlanejamentoRepository extends JpaRepository<ItemBacklogPlanejamento, Long> {

	List<ItemBacklogProjetoSimpleDTO> search(ItemBacklogPlanejamentoFilterDTO filterDTO);

	Long searchCount(ItemBacklogPlanejamentoFilterDTO filterDTO);

	List<ItemBacklogPlanejamento> findBySprintIdAndItemBacklogProjetoId(Long idSprint, Long idItemBacklogSprint); // validar

}
