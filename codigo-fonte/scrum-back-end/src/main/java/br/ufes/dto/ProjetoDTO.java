package br.ufes.dto;

import java.math.BigDecimal;

import br.ufes.enums.EventoFinalizacaoProjetoEnum;
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

	private Integer quantidadeDiasSprint;

	private BigDecimal duracaoMinutosDaily;

	private BigDecimal duracaoMinutosPlanning;

	private BigDecimal duracaoMinutosReview;

	private BigDecimal duracaoMinutosRetrospective;

	private SituacaoProjetoEnum situacao;
	
	private EventoFinalizacaoProjetoEnum eventoFinalizacao;

}
