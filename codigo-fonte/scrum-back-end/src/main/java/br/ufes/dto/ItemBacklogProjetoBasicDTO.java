package br.ufes.dto;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemBacklogProjetoBasicDTO {

	private Long id;

	private String titulo;

	@JsonIgnore
	private Long codigo;
	
	@JsonIgnore
	private CategoriaItemProjetoEnum categoria;
	
	private SituacaoItemProjetoEnum situacao;
	
	@JsonProperty(access = Access.READ_ONLY)
	public String getCodigo() {
		if (ObjectUtils.isEmpty(situacao) || ObjectUtils.isEmpty(categoria)) {
			return "";
		}
		return categoria.getSigla() + codigo;
	}
	
}
