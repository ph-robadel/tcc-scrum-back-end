package br.ufes.dto.filter;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.BaseFilterSearch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogSprintFilterDTO extends BaseFilterSearch {

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;

}
