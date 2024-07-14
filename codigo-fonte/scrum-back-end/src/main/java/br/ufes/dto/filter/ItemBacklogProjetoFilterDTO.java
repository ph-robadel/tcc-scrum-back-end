package br.ufes.dto.filter;

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

}
