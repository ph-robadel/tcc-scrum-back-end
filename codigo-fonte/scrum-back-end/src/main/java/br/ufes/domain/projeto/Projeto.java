package br.ufes.domain.projeto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "projeto")
public class Projeto {

	@Id
	private Long id;

	private String nome;
	
	private String descricao;
	
	private Integer diasSprint;
	
	private Integer minutosDaily;
	
	private Integer minutosPlanning;
	
	private Integer minutosReview;
}
