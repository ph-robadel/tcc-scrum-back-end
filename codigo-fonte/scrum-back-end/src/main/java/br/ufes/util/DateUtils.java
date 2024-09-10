package br.ufes.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class DateUtils {

	public static LocalDate adicionarDiasUteis(LocalDate dataInicial, int diasUteis) {
		LocalDate dataFinal = dataInicial;
		var listaDiasNaoUteis = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
		var diasAdicionados = listaDiasNaoUteis.contains(dataInicial.getDayOfWeek()) ? 0 : 1;

		while (diasAdicionados < diasUteis) {
			dataFinal = dataFinal.plusDays(1);

			if (!listaDiasNaoUteis.contains(dataFinal.getDayOfWeek())) {
				diasAdicionados++;
			}
		}

		return dataFinal;
	}
}
