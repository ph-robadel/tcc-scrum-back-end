package br.ufes.dto.filter;

import br.ufes.enums.PerfilUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.BaseFilterSearch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoUsuarioFilterDTO extends BaseFilterSearch {

	private String nomeUsuario;
	private PerfilUsuarioEnum perfil;
	private Boolean ativo;

}
