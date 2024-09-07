package br.ufes.entity;

import java.util.List;

import br.ufes.dto.SprintPlanningDTO;
import jakarta.persistence.CascadeType;
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

	@OneToMany(mappedBy = "sprintPlanning", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CapacidadeUsuario> capacidadeTime;

	private String objetivo;

	@OneToMany(mappedBy = "sprintPlanning", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemBacklogPlanning> itensSelecionados;
	
	public void atualizarAtributos(SprintPlanningDTO sprintPlanningDTO) {
		super.atualizarAtributos(sprintPlanningDTO);
		this.objetivo = sprintPlanningDTO.getObjetivo();
	}

}
