package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import br.ufes.entity.SprintDaily;

@Repository
public interface SprintDailyRepository extends JpaRepository<SprintDaily, Long> {

	List<SprintDailyBasicDTO> search(SprintDailyFilterDTO filterDTO);

	Long searchCount(SprintDailyFilterDTO filterDTO);

}
