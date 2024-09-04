package br.ufes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "REGISTRO_DAILY")
public class RegistroDaily {

	@Id
	@Column(name = "ID_REGISTRO_DAILY")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_AUTOR")
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERGUNTA_RESPOSTA")
	private PerguntaRespostaDaily perguntaResposta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT_DAILY")
	private SprintDaily sprintDaily;

}
