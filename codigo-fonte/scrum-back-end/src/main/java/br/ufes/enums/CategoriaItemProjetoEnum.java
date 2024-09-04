package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoriaItemProjetoEnum {
	REQUISITO_FUNCIONAL("RF"), REQUISITO_NAO_FUNCIONAL("RNF"), BUG("B"), MELHORIA("M");

	private String sigla;

	public static CategoriaItemProjetoEnum fromString(String valor) {
		if (StringUtils.isEmpty(valor)) {
			return null;
		}
		var valorFormatado = valor.toUpperCase().trim();
		try {
			return valueOf(valorFormatado);
		} catch (IllegalArgumentException ex) {
			throw new RequestArgumentException("A categoria '" + valor + "' não é válida");
		}
	}

	public static CategoriaItemProjetoEnum fromSigla(String sigla) {
		if (StringUtils.isEmpty(sigla)) {
			return null;
		}
		var siglaFormatada = sigla.toUpperCase().trim();
		for (CategoriaItemProjetoEnum categoria : CategoriaItemProjetoEnum.values()) {
			if (categoria.getSigla().equalsIgnoreCase(siglaFormatada)) {
				return categoria;
			}
		}
		throw new RequestArgumentException("A sigla '" + siglaFormatada + "' não é inválida");
	}

}
