package br.ufes.dto;

import br.ufes.enums.SituacaoItemProjetoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBacklogProjetoUpdateDTO {

	private String titulo;

	private String descricao;

	private SituacaoItemProjetoEnum situacao;

}
