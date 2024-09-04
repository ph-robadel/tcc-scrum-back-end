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
@Table(name = "ITEM_BACKLOG_PLANNING")
public class ItemBacklogPlanning {
	
	@Id
	@Column(name = "ID_ITEM_BACKLOG_PLANNING")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String descricao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ITEM_BACKLOG_PROJETO")
	private ItemBacklogProjeto itemBacklogProjeto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT_PLANNING")
	private SprintPlanning sprintPlanning;

}
