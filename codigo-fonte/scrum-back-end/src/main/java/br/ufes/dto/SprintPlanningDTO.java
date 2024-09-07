package br.ufes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintPlanningDTO extends EventoDTO {

	private List<CapacidadeUsuarioDTO> capacidadeTime;

	private String objetivo;

	private List<ItemBacklogPlanningDTO> itensSelecionados;
	
}
