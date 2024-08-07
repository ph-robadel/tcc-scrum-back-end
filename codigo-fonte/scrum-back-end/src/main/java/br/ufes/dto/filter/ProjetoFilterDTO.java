package br.ufes.dto.filter;

import java.util.HashMap;

import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoFilterDTO extends BaseFilterSearch {

	private String nome;
	
	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}
}
