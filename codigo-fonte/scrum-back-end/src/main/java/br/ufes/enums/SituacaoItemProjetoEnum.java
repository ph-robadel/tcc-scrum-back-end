package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoItemProjetoEnum {
	REDIGINDO("REDIGINDO"),
	ESCRITA_FINALIZADA("Em análise"),
	APROVADO("Aprovado"),
	CONCLUIDO("Concluído");

	private String situacao;
	
	public static SituacaoItemProjetoEnum fromString(String valor) {
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
