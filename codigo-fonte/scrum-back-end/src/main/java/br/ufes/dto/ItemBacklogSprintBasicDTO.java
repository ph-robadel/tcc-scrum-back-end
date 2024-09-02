package br.ufes.dto;

import java.math.BigDecimal;

import br.ufes.enums.SituacaoItemSprintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogSprintBasicDTO {

	private Long id;

	private UsuarioBasicDTO responsavelRealizacao;

	private SprintBasicDTO sprint;

	private BigDecimal horasEstimadas;

	private BigDecimal horasRealizadas;

	private SituacaoItemSprintEnum situacao;

	private Long prioridade;

	private ItemBacklogProjetoSimpleDTO itemBacklogProjeto;
}
