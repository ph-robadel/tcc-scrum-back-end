package br.ufes.facade;

import org.springframework.stereotype.Component;

import br.ufes.domain.projeto.ProjetoDTO;

@Component
public class ProjetoFacade {

	public ProjetoDTO getAllByUserLogin() throws Exception {
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
