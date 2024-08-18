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
public class ProjetoUsuarioFilterDTO extends BaseFilterSearch {

	private Long idProjeto;
	private String nomeUsuario;
	private PerfilUsuarioEnum perfil;
	private Boolean apenasAtivo;

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");
		mapFieldSort.put("perfil", "perfil");
		mapFieldSort.put("nome", "nomeUsuario");
		mapFieldSort.put("nomeusuario", "nomeUsuario");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

	public ProjetoUsuarioFilterDTO(String nomeUsuario, String perfil, Boolean apenasAtivo) {
		this.nomeUsuario = nomeUsuario;
		this.perfil = PerfilUsuarioEnum.fromString(perfil);
		this.apenasAtivo = apenasAtivo;
	}

}
