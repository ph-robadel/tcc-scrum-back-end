package br.ufes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.ufes.dto.ItemBacklogSprintUpsertDTO;
import br.ufes.enums.SituacaoItemSprintEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "ITEM_BACKLOG_SPRINT")
public class ItemBacklogSprint {
	
	@Id
	@Column(name = "ID_ITEM_BACKLOG_SPRINT")
	@GeneratedValue
	private Long id;
	
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_REALIZACAO")
	private Usuario responsavelRealizacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SPRINT")
	private Sprint sprint;

	private BigDecimal horasEstimadas;

	private BigDecimal horasRealizadas;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ITEM_BACKLOG_PROJETO")
	private ItemBacklogProjeto itemBacklogProjeto;

	@Enumerated(EnumType.STRING)
	private SituacaoItemSprintEnum situacao;

	private Long prioridade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private Usuario responsavelInclusao;

	private LocalDateTime dataInclusao;

	public void atualizarAtributos(ItemBacklogSprintUpsertDTO itemBacklogSprintUpsertDTO) {
		this.descricao = itemBacklogSprintUpsertDTO.getDescricao();
		this.horasEstimadas = itemBacklogSprintUpsertDTO.getHorasEstimadas();
		this.horasRealizadas = itemBacklogSprintUpsertDTO.getHorasRealizadas();
		this.situacao = itemBacklogSprintUpsertDTO.getSituacao();
	}

}
