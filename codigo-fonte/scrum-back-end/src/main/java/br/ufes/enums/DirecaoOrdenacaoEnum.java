package br.ufes.enums;

public enum DirecaoOrdenacaoEnum {
	ASC, DESC;

	public static DirecaoOrdenacaoEnum fromString(String valor) {
		if (valor == null) {
			return null;
		}
		var valorFormatado = valor.toUpperCase().trim();
		return valueOf(valorFormatado);
	}
}
