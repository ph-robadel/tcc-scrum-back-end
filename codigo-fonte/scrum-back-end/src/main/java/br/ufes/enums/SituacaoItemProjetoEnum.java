package br.ufes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoItemProjetoEnum {
	EM_ANÁLISE("Em análise"),
	APROVADO("Aprovado"),
	CONCLUIDO("Concluído");

	private String situacao;
}
