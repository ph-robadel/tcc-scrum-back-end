package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PerfilUsuarioEnum {
	
	SCRUM_MASTER("Scrum Master"),
	PRODUCT_OWNER("Product Owner"),
	DEV_TEAM("Time de desenvolvimento"),
	ADMINISTRADOR("Administrador do Sistema");

	private String role;

	public static PerfilUsuarioEnum fromString(String valor) {
		if (StringUtils.isEmpty(valor)) {
			return null;
		}
		var valorFormatado = valor.toUpperCase().trim();
		try {
			return valueOf(valorFormatado);
		} catch (IllegalArgumentException ex) {
			throw new RequestArgumentException("O perfil '" + valor + "' não é válido");
		}
	}
}
