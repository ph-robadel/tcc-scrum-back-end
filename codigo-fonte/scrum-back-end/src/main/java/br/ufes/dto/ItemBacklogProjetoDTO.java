package br.ufes.dto;

import java.time.LocalDateTime;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBacklogProjetoDTO {

	private Long id;

	private String titulo;

	@JsonIgnore
	private Long codigo;

	private String descricao;

	private Long prioridade;

	private SituacaoItemProjetoEnum situacao;

	private CategoriaItemProjetoEnum categoria;

	private LocalDateTime dataCriacao;

	private UsuarioBasicDTO autor;

	@JsonIgnore
	private ProjetoBasicDTO projeto;

	@JsonProperty(access = Access.READ_ONLY)
	public String getCodigo() {
		if (ObjectUtils.isEmpty(situacao) || ObjectUtils.isEmpty(categoria)) {
			return "";
		}
		return categoria.getSigla() + codigo;
	}

}
