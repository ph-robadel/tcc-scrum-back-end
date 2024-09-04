package br.ufes.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
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
@Table(name = "SPRINT_REVIEW")
public class SprintReview extends Evento {

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ITEM_APROVADO_REVIEW", joinColumns = @JoinColumn(name = "ID_ITEM_REVIEW"), inverseJoinColumns = @JoinColumn(name = "ID_EVENTO"))
	private List<ItemReview> itensAprovados;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ITEM_REJEITADO_REVIEW", joinColumns = @JoinColumn(name = "ID_ITEM_REVIEW"), inverseJoinColumns = @JoinColumn(name = "ID_EVENTO"))
	private List<ItemReview> itensRejeitados;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ITEM_APROVADO_PARCIALMENTE_REVIEW", joinColumns = @JoinColumn(name = "ID_ITEM_REVIEW"), inverseJoinColumns = @JoinColumn(name = "ID_EVENTO"))
	private List<ItemReview> itensAprovadosParcialmente;

}
