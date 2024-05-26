package br.ufes.enums;

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
}
