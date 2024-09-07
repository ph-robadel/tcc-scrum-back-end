package br.ufes.entity;

import java.math.BigDecimal;
import java.util.List;

import br.ufes.dto.ProjetoUpsertDTO;
import br.ufes.enums.EventoFinalizacaoProjetoEnum;
import br.ufes.enums.SituacaoProjetoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "PROJETO")
public class Projeto {

	@Id
	@Column(name = "ID_PROJETO")
	@GeneratedValue
	private Long id;

	private String nome;

	private String descricao;

	private Integer quantidadeDiasSprint;

	private BigDecimal duracaoHorasDaily;

	private BigDecimal duracaoHorasPlanning;

	private BigDecimal duracaoHorasReview;

	private BigDecimal duracaoHorasRetrospective;

	private SituacaoProjetoEnum situacao;
	
	private EventoFinalizacaoProjetoEnum eventoFinalizacao;

	@OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY)
	private List<Sprint> sprints;

	@OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY)
	private List<ItemBacklogProjeto> backlog;

	@OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY)
	private List<ProjetoUsuario> time;

	public void atualizarAtributos(ProjetoUpsertDTO projetoInsertDTO) {
		setNome(projetoInsertDTO.getNome());
		setDescricao(projetoInsertDTO.getDescricao());
		setQuantidadeDiasSprint(projetoInsertDTO.getQuantidadeDiasSprint());
		setDuracaoHorasDaily(projetoInsertDTO.getDuracaoHorasDaily());
		setDuracaoHorasPlanning(projetoInsertDTO.getDuracaoHorasPlanning());
		setDuracaoHorasReview(projetoInsertDTO.getDuracaoHorasReview());
		setDuracaoHorasRetrospective(projetoInsertDTO.getDuracaoHorasRetrospective());
	}
	
	public boolean isAtivo() {
		return !SituacaoProjetoEnum.CONCLUIDO.equals(situacao) && !SituacaoProjetoEnum.CANCELADO.equals(situacao);
	}

}
