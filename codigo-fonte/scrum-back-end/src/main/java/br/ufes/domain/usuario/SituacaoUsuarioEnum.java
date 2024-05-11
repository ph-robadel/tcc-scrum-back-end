package br.ufes.domain.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoUsuarioEnum {
	INATIVO("Inativo"),
	ATIVO("Ativo");

	private String situacao;
}
