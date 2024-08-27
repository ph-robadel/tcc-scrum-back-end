package br.ufes.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.enums.CategoriaItemProjetoEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.repository.ItemBacklogProjetoRepository;
import br.ufes.util.ResponseSearch;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemBacklogProjetoService {

	@Autowired
	private ItemBacklogProjetoRepository itemBacklogProjetoRepository;

	public ItemBacklogProjetoDTO getMock() throws Exception {
		var itemBackLogProjetoDTO = new ItemBacklogProjetoDTO();
		itemBackLogProjetoDTO.setId(1l);
		itemBackLogProjetoDTO.setTitulo("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setCodigo(1L);
		itemBackLogProjetoDTO.setDescricao("Descrição projeto Scrum");
		itemBackLogProjetoDTO.setPrioridade(1l);
		itemBackLogProjetoDTO.setSituacao(SituacaoItemProjetoEnum.REDIGINDO);
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
	
	public ItemBacklogProjeto getById(Long idItemBacklogProjeto) {
		if (idItemBacklogProjeto == null) {
			return null;
		}

		var usuario = itemBacklogProjetoRepository.findById(idItemBacklogProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

		return usuario;
	}

	public Long obterCodigoNovoItem(Long idProjeto, CategoriaItemProjetoEnum categoria) {
		return itemBacklogProjetoRepository.obterCodigoNovoItem(idProjeto, categoria);
	}
	
	public Long obterNumeroPrioridadeNovoItem(Long idProjeto) {
		return itemBacklogProjetoRepository.obterNumeroPrioridadeNovoItem(idProjeto);
	}

	public ItemBacklogProjeto save(ItemBacklogProjeto itemBacklogProjeto) {
		return itemBacklogProjetoRepository.save(itemBacklogProjeto);
	}

	public ResponseSearch<ItemBacklogProjetoBasicDTO> search(ItemBacklogProjetoFilterDTO filterDTO) {
		var listPage = itemBacklogProjetoRepository.search(filterDTO);
		var total = itemBacklogProjetoRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}
}
