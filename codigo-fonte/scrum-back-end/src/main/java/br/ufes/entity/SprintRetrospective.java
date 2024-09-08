package br.ufes.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "SPRINT_RETROSPECTIVE")
public class SprintRetrospective extends Evento {

	@ElementCollection(targetClass = String.class)
    @Enumerated(EnumType.STRING)
	private List<String> oqueMelhorar;
	
	@ElementCollection(targetClass = String.class)
	@Enumerated(EnumType.STRING)
	private List<String> oqueDeuErrado;
	
	@ElementCollection(targetClass = String.class)
	@Enumerated(EnumType.STRING)
	private List<String> oqueDeuCerto;

}