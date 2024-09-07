package br.ufes.dto;

import java.math.BigDecimal;

import br.ufes.enums.SituacaoProjetoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoDTO {

	private Long id;

	private String nome;

	private String descricao;

	private Integer diasSprint;

	private BigDecimal duracaoHorasDaily;

	private BigDecimal duracaoHorasPlanning;

	private BigDecimal duracaoHorasReview;

	private BigDecimal duracaoHorasRetrospective;
	
	private SituacaoProjetoEnum situacao;

}
