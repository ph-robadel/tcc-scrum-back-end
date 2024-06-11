package br.ufes.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.ProjetoDTO;

@Service
public class ProjetoService {
	
	public ProjetoDTO getMock() throws Exception {
		var projetoMock = new ProjetoDTO();
		projetoMock.setId(1l);
		projetoMock.setNome("Nome projeto scrum");
		projetoMock.setDescricao("Descrição projeto Scrum");
		projetoMock.setDuracaoHorasDaily(new BigDecimal(0.5));
		projetoMock.setDuracaoHorasPlanning(new BigDecimal(2));
		projetoMock.setDuracaoHorasReview(new BigDecimal(1));
		projetoMock.setDuracaoHorasRetrospective(new BigDecimal(1));
		projetoMock.setDiasSprint(10);

		return projetoMock;
	}
	public ProjetoBasicDTO getBasicMock() throws Exception {
		var projetoMock = new ProjetoBasicDTO();
		projetoMock.setId(1l);
		projetoMock.setNome("Nome projeto scrum");
		
		return projetoMock;
	}
}
