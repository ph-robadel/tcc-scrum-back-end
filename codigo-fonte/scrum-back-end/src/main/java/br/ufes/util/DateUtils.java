package br.ufes.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {

	public static LocalDate adicionarDiasUteis(LocalDate dataInicial, int diasUteis) {
		LocalDate dataFinal = dataInicial;
		int diasAdicionados = 1;

		while (diasAdicionados < diasUteis) {
			dataFinal = dataFinal.plusDays(1);

			if (!DayOfWeek.SATURDAY.equals(dataFinal.getDayOfWeek())
					&& !DayOfWeek.SUNDAY.equals(dataFinal.getDayOfWeek())) {
				diasAdicionados++;
			}
		}

		return dataFinal;
	}
}
