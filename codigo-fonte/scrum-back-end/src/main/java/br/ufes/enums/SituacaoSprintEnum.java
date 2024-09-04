package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoSprintEnum {
	CRIADA("Sprint criada"),
	EM_PLANEJAMENTO("Sprint em planejamento"),
	EM_ANDAMENTO("Sprint em andamento"),
	CONCLUIDA("Sprint concluída"),
	CANCELADA("Sprint cancelada");

	private String situacao;
	
	public static SituacaoSprintEnum fromString(String valor) {
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
