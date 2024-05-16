package br.ufes.facade;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.SprintDTO;

@Component
public class SprintFacade {

	public SprintDTO getAllByProjeto() throws Exception {
		var sprintMock = new SprintDTO();
		sprintMock.setId(1l);
		sprintMock.setNumero(1);
		sprintMock.setDataInicio(LocalDate.now());
		sprintMock.setDataFim(LocalDate.now().plusDays(15));
		sprintMock.setProjeto(new ProjetoDTO(1l));
		
		return sprintMock;
	}

}
