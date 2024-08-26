package br.ufes.dto;

import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemBacklogProjetoInsertDTO {

	private String titulo;

	private String descricao;

	private String situacao;
	
	private String categoria;
	
	public SituacaoItemProjetoEnum getSituacao() {
		return SituacaoItemProjetoEnum.fromString(situacao);
	}
	
	public CategoriaItemProjetoEnum getCategoria() {
		return CategoriaItemProjetoEnum.fromString(categoria);
	}
	
	

}
