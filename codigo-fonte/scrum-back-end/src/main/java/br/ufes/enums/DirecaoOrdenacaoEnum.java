package br.ufes.enums;

import br.ufes.exception.RequestArgumentException;

public enum DirecaoOrdenacaoEnum {
	ASC, DESC;

	public static DirecaoOrdenacaoEnum fromString(String valor) {
		if (valor == null) {
			return null;
		}
		var valorFormatado = valor.toUpperCase().trim();
		try {
			return valueOf(valorFormatado);
		} catch (IllegalArgumentException ex) {
			throw new RequestArgumentException("O sortOrder '" + valor + "' não é válido");
		}
	}
}
