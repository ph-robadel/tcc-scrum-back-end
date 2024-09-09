package br.ufes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemReviewDTO {

	@JsonIgnore
	private Long id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long idItemBacklogProjeto;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private ItemBacklogProjetoBasicDTO itemBacklogProjeto;

	private String descricao;

}
