package br.ufes.util;

import br.ufes.enums.DirecaoOrdenacaoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFilterSearch {

	private Integer page;
	private Integer size;
	private String fieldSort;
	private DirecaoOrdenacaoEnum sortOrder;
	
	public void setPageAndSorting(Integer page, Integer size, String fieldSort, String sortOrder) {
		this.page = page;
		this.size = size;
		this.fieldSort = fieldSort;
		setSortOrder(DirecaoOrdenacaoEnum.fromString(sortOrder));
	}
	
	
}
