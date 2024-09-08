package br.ufes.dto.filter;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintDailyFilterDTO extends BaseFilterSearch {

	@JsonIgnore
	private Long idSprint;

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");
		mapFieldSort.put("nome", "nome");
		mapFieldSort.put("data", "inicio");
		mapFieldSort.put("inicio", "inicio");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

}
