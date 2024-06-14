package br.ufes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.ufes.enums.SituacaoItemSprintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogSprintUpsertDTO {

	private LocalDate dataInicio;

	private LocalDate dataFim;
	
	private BigDecimal horasEstimadas;
	
	private BigDecimal horasRealizadas;
	
	private SituacaoItemSprintEnum situacao;
	
	private String descricaoBloqueio;
	
	private Long prioridade;

}
