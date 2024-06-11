package br.ufes.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoUpsertDTO {

	private String nome;

	private String descricao;

	private Integer diasSprint;

	private BigDecimal duracaoHorasDaily;

	private BigDecimal duracaoHorasPlanning;

	private BigDecimal duracaoHorasReview;

	private BigDecimal duracaoHorasRetrospective;
	
}
