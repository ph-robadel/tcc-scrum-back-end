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
public class ItemBacklogSprintExportDTO {

	private Long id;
	
	private String descricao;

	private UsuarioBasicDTO responsavelRealizacao;

	private BigDecimal horasEstimadas;

	private BigDecimal horasRealizadas;

	private ItemBacklogProjetoBasicDTO itemBacklogProjeto;

	private SituacaoItemSprintEnum situacao;

	private Long prioridade;

	private UsuarioBasicDTO responsavelInclusao;

	private LocalDateTime dataInclusao;


}
