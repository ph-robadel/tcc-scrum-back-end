package br.ufes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.ufes.dto.EventoDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Evento {

	@Id
	@Column(name = "ID_EVENTO")
	@GeneratedValue
	private Long id;
	
	private String nome;

	private String descricao;

	@ElementCollection(targetClass = String.class)
	@Enumerated(EnumType.STRING)
	private List<String> anexos;

	@OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventoUsuario> participantes;

	private LocalDateTime inicio;

	private LocalDateTime fim;

	private BigDecimal duracaoMinutos;
	
	public void atualizarAtributos(EventoDTO eventoDTO) {
		this.nome = eventoDTO.getNome();
		this.descricao = eventoDTO.getDescricao();
		this.anexos = eventoDTO.getAnexos();
		this.inicio = eventoDTO.getInicio();
		this.fim = eventoDTO.getFim();
		this.duracaoMinutos = eventoDTO.getDuracaoMinutos();
	}

}
