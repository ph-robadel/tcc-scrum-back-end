package br.ufes.dto;

import java.math.BigDecimal;

import br.ufes.enums.EventoFinalizacaoProjetoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoUpsertDTO {

	private String nome;

	private String descricao;

	private Integer quantidadeDiasSprint;

	private BigDecimal duracaoMinutosDaily;

	private BigDecimal duracaoMinutosPlanning;

	private BigDecimal duracaoMinutosReview;

	private BigDecimal duracaoMinutosRetrospective;
	
	private EventoFinalizacaoProjetoEnum eventoFinalizacao;
	
}
