package br.ufes.services;

import org.springframework.stereotype.Service;

import br.ufes.dto.ProjetoDTO;

@Service
public class ProjetoService {
	
	public ProjetoDTO getMock() throws Exception {
		var projetoMock = new ProjetoDTO();
		projetoMock.setId(1l);
		projetoMock.setNome("Nome projeto scrum");
		projetoMock.setDescricao("Descrição projeto Scrum");
		projetoMock.setMinutosDaily(30);
		projetoMock.setMinutosPlanning(30);
		projetoMock.setMinutosReview(30);
		projetoMock.setDiasSprint(10);

		return projetoMock;
	}
}
