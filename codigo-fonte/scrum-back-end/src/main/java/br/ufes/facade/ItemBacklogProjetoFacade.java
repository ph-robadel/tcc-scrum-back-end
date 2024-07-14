package br.ufes.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoUpsertDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ProjetoService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;

@Component
public class ItemBacklogProjetoFacade {

	@Autowired
	private ItemBacklogProjetoService itemBackLogProjetoService;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ModelMapper modelMapper;

	public ItemBacklogProjetoDTO cadastrarItemBacklogProjeto(ItemBacklogProjetoUpsertDTO itemBacklogProjetoUpsertDTO) throws Exception {
		var itemBacklogProjetoDTO = modelMapper.map(itemBacklogProjetoUpsertDTO, ItemBacklogProjetoDTO.class);
		itemBacklogProjetoDTO.setCodigo(1l);
		itemBacklogProjetoDTO.setDataCriacao(LocalDateTime.now());
		itemBacklogProjetoDTO.setAutor(usuarioService.getBasicMock());
		itemBacklogProjetoDTO.setProjeto(projetoService.getBasicMock());

		return itemBacklogProjetoDTO;
	}

	public ItemBacklogProjetoDTO atualizarItemBacklogProjeto(Long idItemBacklogProjeto,
			ItemBacklogProjetoUpsertDTO itemBacklogProjetoUpsertDTO) throws Exception {
		var itemBacklogProjetoDTO = modelMapper.map(itemBacklogProjetoUpsertDTO, ItemBacklogProjetoDTO.class);
		itemBacklogProjetoDTO.setCodigo(1l);
		itemBacklogProjetoDTO.setDataCriacao(LocalDateTime.now());
		itemBacklogProjetoDTO.setAutor(usuarioService.getBasicMock());
		itemBacklogProjetoDTO.setProjeto(projetoService.getBasicMock());

		return itemBacklogProjetoDTO;
	}

	public ResponseSearch<ItemBacklogProjetoDTO> search(Long idProjeto, ItemBacklogProjetoFilterDTO filterDTO)
			throws Exception {
		var mock = itemBackLogProjetoService.getMock();
		mock.setProjeto(projetoService.getBasicMock());
		mock.setAutor(usuarioService.getBasicMock());

		var responseSearch = new ResponseSearch<ItemBacklogProjetoDTO>();
		responseSearch.setLista(List.of(mock));
		responseSearch.setTotal(1l);
		return responseSearch;
	}

	public void deleteItemBacklogProjeto(Long idItemBacklogProjeto) throws Exception {
	}

	public ItemBacklogProjetoDTO getById(Long idItemBacklogProjeto) throws Exception {
		var mock = itemBackLogProjetoService.getMock();
		mock.setProjeto(projetoService.getBasicMock());
		mock.setAutor(usuarioService.getBasicMock());
		return mock;
	}

}
