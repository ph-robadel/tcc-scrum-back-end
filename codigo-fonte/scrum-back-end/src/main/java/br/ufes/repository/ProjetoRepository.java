package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import br.ufes.entity.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

	List<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO);

	Long searchCount(ProjetoFilterDTO filterDTO);

}
