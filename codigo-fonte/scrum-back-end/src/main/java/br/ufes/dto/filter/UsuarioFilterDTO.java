package br.ufes.dto.filter;

import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFilterDTO extends BaseFilterSearch {

	private String nome;

	private Boolean apenasAtivo;

	private PerfilUsuarioEnum perfil;

	public UsuarioFilterDTO(String nome, Boolean apenasAtivo, String perfil) {
		this.nome = nome;
		this.apenasAtivo = apenasAtivo;
		setPerfil(PerfilUsuarioEnum.fromString(perfil));
	}

}
