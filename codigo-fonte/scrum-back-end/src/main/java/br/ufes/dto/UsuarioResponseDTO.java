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
public class UsuarioResponseDTO {

	private Long id;

	private String nomeCompleto;

	private String nomeUsuario;

	private String email;

	private Boolean ativo;

	private PerfilUsuarioEnum perfil;

	public UsuarioResponseDTO(Long id, String nomeCompleto, String nomeUsuario, String email, Boolean ativo) {
		this.id = id;
		this.nomeCompleto = nomeCompleto;
		this.nomeUsuario = nomeUsuario;
		this.email = email;
		this.ativo = ativo;
	}
	
	

}
