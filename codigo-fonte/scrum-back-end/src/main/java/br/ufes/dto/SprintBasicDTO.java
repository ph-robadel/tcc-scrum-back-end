package br.ufes.dto;

import br.ufes.enums.SituacaoSprintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintBasicDTO {

	private Long id;
	private Integer numero;
	private SituacaoSprintEnum situacao;

}
