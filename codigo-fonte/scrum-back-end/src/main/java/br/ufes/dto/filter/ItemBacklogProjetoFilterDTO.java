package br.ufes.dto.filter;

import java.util.HashMap;

import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogProjetoFilterDTO extends BaseFilterSearch {

	private String titulo;

	private Long codigo;

	private SituacaoItemProjetoEnum situacao;

	private Long idAutor;
	
	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "id");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

}
