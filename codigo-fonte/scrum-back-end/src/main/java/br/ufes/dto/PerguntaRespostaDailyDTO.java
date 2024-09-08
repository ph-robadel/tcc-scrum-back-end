package br.ufes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerguntaRespostaDailyDTO {

	@JsonIgnore
	private Long id;
	
	private String pergunta;

	private String resposta;

	

}
