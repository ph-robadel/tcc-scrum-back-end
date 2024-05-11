package br.ufes.domain.projeto;

import java.util.List;

import br.ufes.domain.sprint.Sprint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PROJETO")
	private Long id;

	private String nome;

	private String descricao;

	private Integer diasSprint;

	private Integer minutosDaily;

	private Integer minutosPlanning;

	private Integer minutosReview;

	@OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY)
	private List<Sprint> sprints;

}
