package br.ufes.dto;

import java.time.LocalDateTime;

import br.ufes.enums.SituacaoItemProjetoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBackLogProjetoDTO {

	private Long id;

	private String titulo;

	private Long codigo;

	private String descricao;

	private Integer prioridade;

	private SituacaoItemProjetoEnum situacao;

	private LocalDateTime dataCriacao;

	private UsuarioBasicDTO autor;

	private ProjetoBasicDTO projeto;

}
