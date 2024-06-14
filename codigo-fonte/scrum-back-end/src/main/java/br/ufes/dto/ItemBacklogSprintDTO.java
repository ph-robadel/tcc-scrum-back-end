package br.ufes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.ufes.enums.SituacaoItemSprintEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBacklogSprintDTO {

	private Long id;

	private UsuarioBasicDTO responsavelRealizacao;

	private SprintBasicDTO sprint;

	private BigDecimal horasEstimadas;

	private BigDecimal horasRealizadas;

	private ItemBacklogProjetoBasicDTO itemBacklogProjeto;

	private SituacaoItemSprintEnum situacao;

	private String descricaoBloqueio;

	private Long prioridade;

	private Boolean pendenteAprovacao;
	
	private Boolean pendenteRemocao;

	private UsuarioBasicDTO responsavelInclusao;

	private LocalDateTime dataInclusao;

	private UsuarioBasicDTO responsavelAprovacao;

	private LocalDateTime dataAprovacao;

}
