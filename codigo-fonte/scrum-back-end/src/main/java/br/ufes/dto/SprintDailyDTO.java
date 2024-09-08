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
public class SprintDailyDTO extends EventoDTO {

	private List<RegistroDailyDTO> registroDaily;
	
}
