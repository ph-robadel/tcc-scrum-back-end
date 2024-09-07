package br.ufes.dto;

import java.time.LocalDate;

import br.ufes.enums.SituacaoSprintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintDTO {

	private Long id;

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;
	
	private SituacaoSprintEnum situacao;

	private ProjetoBasicDTO projeto;
	

}
