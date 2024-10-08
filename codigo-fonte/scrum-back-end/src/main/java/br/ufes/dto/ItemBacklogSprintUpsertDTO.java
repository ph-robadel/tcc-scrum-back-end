package br.ufes.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private String descricao;

	private Long idResponsavelRealizacao;
	
	private BigDecimal horasEstimadas;

	private BigDecimal horasRealizadas;
	
	private SituacaoItemSprintEnum situacao;

	@JsonIgnore
	private Long idSprint;

	@JsonIgnore
	private Long idItemBacklogProjeto;

}
