package br.ufes.domain.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

	private Long Id;

	private String nomeCompleto;

	private String nomeUsuario;

	private String senha;

	private String email;

	private Boolean ativo;

	private PerfilUsuarioEnum perfil;

}
