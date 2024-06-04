package br.ufes.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.enums.SituacaoItemProjetoEnum;

@Service
public class ItemBacklogProjetoService {

	public ItemBacklogProjetoDTO getMock() throws Exception {
		var itemBackLogProjetoDTO = new ItemBacklogProjetoDTO();
		itemBackLogProjetoDTO.setId(1l);
		itemBackLogProjetoDTO.setTitulo("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setCodigo(1L);
		itemBackLogProjetoDTO.setDescricao("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setPrioridade(1);
		itemBackLogProjetoDTO.setSituacao(SituacaoItemProjetoEnum.EM_ANALISE);
		itemBackLogProjetoDTO.setDataCriacao(LocalDateTime.now());

		return itemBackLogProjetoDTO;
	}

	public ItemBacklogProjetoBasicDTO getBasicMock() throws Exception {
		var itemBacklogProjetoBasicDTO = new ItemBacklogProjetoBasicDTO();
		itemBacklogProjetoBasicDTO.setId(1l);
		itemBacklogProjetoBasicDTO.setTitulo("Descrição projeto Scrum");
		itemBacklogProjetoBasicDTO.setCodigo(1L);

		return itemBacklogProjetoBasicDTO;
	}
}
