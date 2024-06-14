package br.ufes.entity;

import java.time.LocalDateTime;

import br.ufes.enums.SituacaoItemProjetoEnum;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "ITEM_BACKLOG_PROJETO")
public class ItemBacklogProjeto {

	@Id
	@Column(name = "ID_ITEM_BACKLOG_PROJETO")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String titulo;

	@Column(unique = true)
	private Long codigo;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "TEXT")
	private String descricao;

	private Long prioridade;

	@Enumerated(EnumType.STRING)
	private SituacaoItemProjetoEnum situacao;

	private LocalDateTime dataCriacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_AUTOR")
	private Usuario autor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROJETO")
	private Projeto projeto;

}
