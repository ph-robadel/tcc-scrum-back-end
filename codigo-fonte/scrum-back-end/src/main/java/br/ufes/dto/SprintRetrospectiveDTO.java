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
public class SprintRetrospectiveDTO extends EventoDTO {

	private List<String> oqueMelhorar;
	private List<String> oqueDeuErrado;
	private List<String> oqueDeuCerto;
	
}
