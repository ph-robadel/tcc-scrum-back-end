package br.ufes.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import br.ufes.dto.ProjetoDTO;
import br.ufes.dto.SprintDTO;

@Service
public class SprintService {

	public SprintDTO getMock() throws Exception {
		var sprintMock = new SprintDTO();
		sprintMock.setId(1l);
		sprintMock.setNumero(1);
		sprintMock.setDataInicio(LocalDate.now());
		sprintMock.setDataFim(LocalDate.now().plusDays(15));

		return sprintMock;
	}

}
