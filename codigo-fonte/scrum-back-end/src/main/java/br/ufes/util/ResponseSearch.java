package br.ufes.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSearch<T> {

	private List<T> listPage;
	private Long total;
	
	public ResponseSearch(List<T> listPage, Long total) {
		this.listPage = listPage;
		this.total = total;
	}
	
	
}
