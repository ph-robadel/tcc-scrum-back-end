package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoProjetoEnum {
	NOVO("Novo"),
	EM_ANDAMENTO("Sprint em andamento"),
	CONCLUIDO("Sprint concluída"),
	CANCELADO("Sprint cancelada");

	private String situacao;
	
	public static SituacaoProjetoEnum fromString(String valor) {
		if (StringUtils.isEmpty(valor)) {
			return null;
		}
		var valorFormatado = valor.toUpperCase().trim();
		try {
			return valueOf(valorFormatado);
		} catch (IllegalArgumentException ex) {
			throw new RequestArgumentException("A situação '" + valor + "' não é válida");
		}
	}
}
