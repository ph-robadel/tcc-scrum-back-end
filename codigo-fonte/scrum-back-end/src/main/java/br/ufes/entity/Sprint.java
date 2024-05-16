package br.ufes.entity;

import java.time.LocalDate;

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
@Table(name = "sprint")
public class Sprint {

	@Id
	@Column(name = "ID_SPRINT")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

	private Integer numero;

	private LocalDate dataInicio;

	private LocalDate dataFim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROJETO")
	private Projeto projeto;

}
