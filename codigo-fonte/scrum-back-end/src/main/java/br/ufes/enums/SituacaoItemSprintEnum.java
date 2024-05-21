package br.ufes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoItemSprintEnum {
	A_FAZER("A fazer"),
	DESENVOLVENDO("Desenvolvendo"),
	BLOQUEADO("Bloqueado"),
	TESTANDO("Testando"),
	PRONTO("Pronto");

	private String situacao;
}
