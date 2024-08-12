package br.ufes.dto;

import br.ufes.enums.PerfilUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpsertDTO {

	private String nomeCompleto;

	private String nomeUsuario;
	
	private String senha;
	
	private String email;

	private String perfil;
	
	public String getNomeUsuario() {
		return nomeUsuario != null ? nomeUsuario.toLowerCase() : "";
	}
	
	public PerfilUsuarioEnum getPerfil() {
		return PerfilUsuarioEnum.fromString(perfil);
	}

}
