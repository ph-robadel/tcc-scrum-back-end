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
public class UsuarioFilterDTO extends BaseFilterSearch{

	private String nomeCompleto;

	private String nomeUsuario;

	private String email;

	private Boolean ativo;

	private PerfilUsuarioEnum perfil;

}
