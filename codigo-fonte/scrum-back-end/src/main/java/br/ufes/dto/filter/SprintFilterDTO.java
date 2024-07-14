package br.ufes.dto.filter;

import java.time.LocalDate;

import br.ufes.util.BaseFilterSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintFilterDTO extends BaseFilterSearch {

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;

}
