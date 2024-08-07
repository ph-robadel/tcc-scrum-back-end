package br.ufes.util;

import br.ufes.enums.DirecaoOrdenacaoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFilterSearch {

	protected Integer page;
	protected Integer size;
	protected String fieldSort;
	protected DirecaoOrdenacaoEnum sortOrder;
	
	public void setPageAndSorting(Integer page, Integer size, String fieldSort, String sortOrder) {
		this.page = page;
		this.size = size;
		setFieldSort(fieldSort);
		setSortOrder(DirecaoOrdenacaoEnum.fromString(sortOrder));
	}
	
	public abstract void setFieldSort(String nomeCampo);
	
	
}
