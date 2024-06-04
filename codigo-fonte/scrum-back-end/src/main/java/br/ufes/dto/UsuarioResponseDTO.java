package br.ufes.dto;

import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.enums.SituacaoUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

	private Long Id;

	private String nomeCompleto;

	private String nomeUsuario;

	private String email;

	private SituacaoUsuarioEnum situacao;

	private PerfilUsuarioEnum perfil;

}
