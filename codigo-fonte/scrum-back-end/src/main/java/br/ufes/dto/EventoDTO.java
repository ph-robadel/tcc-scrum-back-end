package br.ufes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class EventoDTO {
	
	@JsonIgnore
	private Long id;
	
	private String nome;

	private String descricao;

	private List<String> anexos;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<Long> idsParticipantes;

	@JsonIgnore
	private List<EventoUsuarioDTO> participantes;

	private LocalDateTime inicio;

	private LocalDateTime fim;

	private BigDecimal duracaoMinutos;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public List<UsuarioBasicDTO> getParticipantes() {
		if(ObjectUtils.isEmpty(participantes)) {
			return null;
		}
		
		return participantes.stream().map(EventoUsuarioDTO::getUsuario).collect(Collectors.toList());
	}
	
}
