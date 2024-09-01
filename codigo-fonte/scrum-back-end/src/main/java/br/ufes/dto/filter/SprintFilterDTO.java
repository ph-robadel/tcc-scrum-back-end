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
public class SprintFilterDTO extends BaseFilterSearch {

	private Integer numero;
	
	@JsonIgnore
	private Long idProjeto;
	
	public SprintFilterDTO(Integer numero) {
		this.numero = numero;
	}
	
	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");
		mapFieldSort.put("numero", "numero");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}	
	
}
