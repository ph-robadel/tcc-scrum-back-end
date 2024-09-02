package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoItemSprintEnum {
	A_FAZER("A fazer"),
	DESENVOLVENDO("Desenvolvendo"),
	BLOQUEADO("Bloqueado"),
	PRONTO("Pronto");

	private String situacao;
	
	public static SituacaoItemSprintEnum fromString(String valor) {
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
