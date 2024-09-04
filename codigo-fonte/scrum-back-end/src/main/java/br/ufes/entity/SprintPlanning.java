package br.ufes.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "SPRINT_PLANNING")
public class SprintPlanning extends Evento {

	@OneToMany(mappedBy = "sprintPlanning", fetch = FetchType.LAZY)
	private List<CapacidadeUsuario> capacidadeTime;

	private String objetivo;

	private String descricao;

	@OneToMany(mappedBy = "sprintPlanning", fetch = FetchType.LAZY)
	private List<ItemBacklogPlanning> itensSelecionados;

}
