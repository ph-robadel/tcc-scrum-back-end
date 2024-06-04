package br.ufes.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ProjetoService;
import br.ufes.services.UsuarioService;

@Component
public class ItemBacklogProjetoFacade {

	@Autowired
	private ItemBacklogProjetoService itemBackLogProjetoService;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private UsuarioService usuarioService;

	public ItemBacklogProjetoDTO search() throws Exception {
		var mock = itemBackLogProjetoService.getMock();
		mock.setProjeto(projetoService.getBasicMock());
		mock.setAutor(usuarioService.getBasicMock());
		return mock;
	}

}
