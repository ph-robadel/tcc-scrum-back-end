package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.enums.CategoriaItemProjetoEnum;

@Repository
public interface ItemBacklogProjetoRepository extends JpaRepository<ItemBacklogProjeto, Long> {

	List<ItemBacklogProjetoBasicDTO> search(ItemBacklogProjetoFilterDTO filterDTO);

	Long searchCount(ItemBacklogProjetoFilterDTO filterDTO);

	Long obterCodigoNovoItem(Long idProjeto, CategoriaItemProjetoEnum categoria);

	Long obterNumeroPrioridadeNovoItem(Long idProjeto);

	void diminuirPrioridadeItem(Long idItemBacklogProjeto, Long antigaPrioridade, Long novaPrioridade);

	void aumentarPrioridadeItem(Long idItemBacklogProjeto, Long antigaPrioridade, Long novaPrioridade);

	void repriorizarDeleteItem(Long itemBacklogProjeto);

}
