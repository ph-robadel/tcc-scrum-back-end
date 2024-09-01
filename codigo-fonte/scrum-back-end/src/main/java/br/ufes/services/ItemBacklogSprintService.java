package br.ufes.services;

import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogSprintDTO;
import br.ufes.entity.Sprint;
import br.ufes.enums.SituacaoItemSprintEnum;

@Service
public class ItemBacklogSprintService {

	public ItemBacklogSprintDTO getMock() throws Exception {
		var itemBacklogSprintDTO = new ItemBacklogSprintDTO();
		itemBacklogSprintDTO.setId(1l);
		itemBacklogSprintDTO.setHorasEstimadas(null);
		itemBacklogSprintDTO.setHorasRealizadas(null);
		itemBacklogSprintDTO.setSituacao(SituacaoItemSprintEnum.A_FAZER);
		itemBacklogSprintDTO.setDescricaoBloqueio(null);
		itemBacklogSprintDTO.setPrioridade(null);
		itemBacklogSprintDTO.setDataInclusao(null);
		itemBacklogSprintDTO.setDataAprovacao(null);

		return itemBacklogSprintDTO;
	}

	public void criarItensNovaSprint(Sprint sprint) {
		
	}
}
