package br.ufes.services;

import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBackLogProjetoDTO;

@Service
public class ItemBackLogProjetoService {

	public ItemBackLogProjetoDTO getMock() throws Exception {
		var itemBackLogProjetoDTO = new ItemBackLogProjetoDTO();
		itemBackLogProjetoDTO.setId(1l);
		itemBackLogProjetoDTO.setTitulo("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setDescricao("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setCodigo(1L);

		return itemBackLogProjetoDTO;
	}
}
