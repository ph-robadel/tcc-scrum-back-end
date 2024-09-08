package br.ufes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintReviewDTO extends EventoDTO {

	private List<ItemReviewDTO> itensAprovados;
	private List<ItemReviewDTO> itensRejeitados;
	private List<ItemReviewDTO> itensAprovadosParcialmente;
	
}
