package br.ufes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjetoBasicDTO {
	
	private Long id;

	private String nome;
	
	private String descricao;

}
