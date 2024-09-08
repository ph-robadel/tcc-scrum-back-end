package br.ufes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemReview {
	
	@Id
	@Column(name = "ID_ITEM_REVIEW")
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ITEM_BACKLOG_PROJETO")
	private ItemBacklogProjeto itemBacklogProjeto;

	private String descricao;

}
