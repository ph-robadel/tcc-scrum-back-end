package br.ufes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBacklogPlanejamentoExportDTO {

	private Long id;

	private ItemBacklogProjetoBasicDTO itemBacklogProjeto;

}
