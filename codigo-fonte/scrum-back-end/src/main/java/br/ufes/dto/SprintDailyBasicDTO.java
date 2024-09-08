package br.ufes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.util.ObjectUtils;

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
public class SprintDailyBasicDTO {

	private Long id;
	
	private String nome;

	@JsonIgnore
	private LocalDateTime inicio;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public LocalDate getData() {
		return !ObjectUtils.isEmpty(inicio) ? inicio.toLocalDate() : null;
	}
	

}
