package br.ufes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT_PLANNING")
	private SprintPlanning sprintPlanning;

}
