package br.ufes.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "SPRINT_DAILY")
public class SprintDaily extends Evento {

	@OneToMany(mappedBy = "sprintDaily", fetch = FetchType.LAZY)
	private List<RegistroDaily> registroDaily;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT")
	private Sprint sprint;

}
