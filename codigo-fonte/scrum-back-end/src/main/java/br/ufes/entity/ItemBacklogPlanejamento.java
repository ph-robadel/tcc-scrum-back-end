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
@Table(name = "ITEM_BACKLOG_PLANEJAMENTO")
public class ItemBacklogPlanejamento {
	
	@Id
	@Column(name = "ID_ITEM_BACKLOG_PLANEJAMENTO")
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT")
	private Sprint sprint;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ITEM_BACKLOG_PROJETO")
	private ItemBacklogProjeto itemBacklogProjeto;

	public ItemBacklogPlanejamento(Sprint sprint, ItemBacklogProjeto itemBacklogProjeto) {
		this.sprint = sprint;
		this.itemBacklogProjeto = itemBacklogProjeto;
	}
	
	

}
