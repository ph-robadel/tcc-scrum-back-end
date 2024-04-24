package br.ufes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoDTO {
	private Long id;

	private String nome;

	private String descricao;

	private Integer tamanhoSpring;

	private Integer minutosDaily;

	private Integer minutosPlanning;

	private Integer minutosReview;
}
