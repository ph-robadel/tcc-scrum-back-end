package br.ufes.dto;

import java.time.LocalDate;
import java.util.List;

import br.ufes.enums.SituacaoSprintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintExportDTO {

	private Long id;

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;
	
	private SituacaoSprintEnum situacao;
	
	private List<ItemBacklogPlanejamentoExportDTO> backlogPlanejamento;
	
	private List<ItemBacklogSprintExportDTO> backlog;
	
	private SprintPlanningDTO planning;
	
	private List<SprintDailyDTO> dailys;
	
	private SprintReviewDTO review;
	
	private SprintRetrospectiveDTO retrospective;

}
