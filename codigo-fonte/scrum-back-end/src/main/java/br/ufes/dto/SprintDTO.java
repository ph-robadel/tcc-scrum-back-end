package br.ufes.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintDTO {

	private Long Id;

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;

	private ProjetoBasicDTO projeto;

}
