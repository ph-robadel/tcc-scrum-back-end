package br.ufes.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufes.dto.ProjetoDTO;
import br.ufes.services.ProjetoService;

@Component
public class ItemBackLogProjetoFacade {

	@Autowired
	private ProjetoService projetoService;

	public ProjetoDTO getAllByUserLogin() throws Exception {
		return projetoService.getMock();
	}

}
