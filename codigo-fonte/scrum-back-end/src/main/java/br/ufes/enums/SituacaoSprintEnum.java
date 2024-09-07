package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoSprintEnum {
	CRIADA("criada"),
	EM_PLANEJAMENTO("em planejamento"),
	EM_ANDAMENTO("em andamento"),
	CONCLUIDA("concluída"),
	CANCELADA("cancelada");

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
