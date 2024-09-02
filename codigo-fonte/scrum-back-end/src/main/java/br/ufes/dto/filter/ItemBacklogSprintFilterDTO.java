package br.ufes.dto.filter;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufes.enums.SituacaoItemSprintEnum;
import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogSprintFilterDTO extends BaseFilterSearch {

	private String titulo;

	private SituacaoItemSprintEnum situacao;

	private Long idResponsavelRealizacao;

	@JsonIgnore
	private Long idSprint;

	public ItemBacklogSprintFilterDTO(String titulo, String situacao, Long idResponsavelRealizacao) {
		this.titulo = titulo;
		this.situacao = SituacaoItemSprintEnum.fromString(situacao);
		this.idResponsavelRealizacao = idResponsavelRealizacao;
	}

	@Override
	public void setFieldSort(String nomeCampo) {
		var mapFieldSort = new HashMap<String, String>();
		var campoFormatado = nomeCampo.toLowerCase().trim();

		mapFieldSort.put("id", "ibs.id");
		mapFieldSort.put("prioridade", "ibs.prioridade");
		mapFieldSort.put("titulo", "ibp.titulo");
		mapFieldSort.put("situacao", "ibs.situacao");
		mapFieldSort.put("idResponsavelRealizacao", "responsavel.id");

		this.fieldSort = mapFieldSort.getOrDefault(campoFormatado, null);
	}

}
