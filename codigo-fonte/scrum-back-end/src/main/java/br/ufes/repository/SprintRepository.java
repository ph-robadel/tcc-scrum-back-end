package br.ufes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.SprintBasicDTO;
import br.ufes.dto.filter.SprintFilterDTO;
import br.ufes.entity.Sprint;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

	List<SprintBasicDTO> search(SprintFilterDTO filterDTO);

	Long searchCount(SprintFilterDTO filterDTO);

	SprintBasicDTO obterSprintByData(Long idProjeto, LocalDate data);

	List<SprintBasicDTO> obterSprintsIntervaloDatas(Long idProjeto, LocalDate dataInicial, LocalDate dataFinal);

	Integer getProximoNumeroSprintFromProjeto(Long idProjeto);

}
