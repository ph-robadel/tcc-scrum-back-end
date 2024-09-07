package br.ufes.enums;

import org.apache.commons.lang3.StringUtils;

import br.ufes.exception.RequestArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventoFinalizacaoProjetoEnum {
	REVIEW("review"),
	RETROSPECTIVE("retrospective");

	private String evento;
	
	public static EventoFinalizacaoProjetoEnum fromString(String evento) {
		if (StringUtils.isEmpty(evento)) {
			return null;
		}
		var eventoFormatado = evento.toUpperCase().trim();
		try {
			return valueOf(eventoFormatado);
		} catch (IllegalArgumentException ex) {
			throw new RequestArgumentException("O evento '" + evento + "' não é válida");
		}
	}
}
