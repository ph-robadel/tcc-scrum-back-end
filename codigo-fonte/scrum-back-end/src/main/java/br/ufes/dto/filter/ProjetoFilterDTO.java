package br.ufes.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.BaseFilterSearch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoFilterDTO extends BaseFilterSearch {

	private String nome;

}
