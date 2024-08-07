package br.ufes.dto.filter;

import java.util.HashMap;

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

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");
		mapFieldSort.put("nomecompleto", "nomeCompleto");
		mapFieldSort.put("nome", "nomeCompleto");
		mapFieldSort.put("nomeusuario", "nomeUsuario");
		mapFieldSort.put("email", "email");
		mapFieldSort.put("ativo", "ativo");
		mapFieldSort.put("perfil", "perfil");

		super.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

}
