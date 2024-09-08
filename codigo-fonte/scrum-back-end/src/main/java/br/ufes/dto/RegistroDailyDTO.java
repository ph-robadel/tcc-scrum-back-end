package br.ufes.dto;

import java.util.List;

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
public class RegistroDailyDTO {

	@JsonIgnore
	private Long id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long idUsuario;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UsuarioBasicDTO usuario;
	
	private List<PerguntaRespostaDailyDTO> perguntaResposta;

	

}
