package br.ufes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERGUNTA_RESPOSTA_DAILY")
public class PerguntaRespostaDaily {

	@Id
	@Column(name = "ID_PERGUNTA_RESPOSTA_DAILY")
	@GeneratedValue
	private Long id;
	
	private String pergunta;

	private String resposta;

}
