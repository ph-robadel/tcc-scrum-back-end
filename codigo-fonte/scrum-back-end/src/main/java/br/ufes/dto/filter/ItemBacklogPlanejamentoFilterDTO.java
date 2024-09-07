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
public class ItemBacklogPlanejamentoFilterDTO extends BaseFilterSearch {

	private String titulo;

	@JsonIgnore
	private Long idSprint;

	public ItemBacklogPlanejamentoFilterDTO(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "ibpl.id");
		mapFieldSort.put("prioridade", "ibp.prioridade");
		mapFieldSort.put("titulo", "ibp.titulo");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

}
