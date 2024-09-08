package br.ufes.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
	
	@JsonProperty(access = Access.READ_ONLY)
	public Long getId() {
		return super.getId();
	}
	
}
