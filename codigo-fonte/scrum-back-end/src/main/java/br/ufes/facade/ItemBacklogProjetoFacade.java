package br.ufes.facade;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.ItemBacklogProjetoDTO;
import br.ufes.dto.ItemBacklogProjetoInsertDTO;
import br.ufes.dto.ItemBacklogProjetoUpdateDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.enums.PerfilUsuarioEnum;
import br.ufes.enums.SituacaoItemProjetoEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.ItemBacklogProjetoService;
import br.ufes.services.ProjetoService;
import br.ufes.services.UsuarioService;
import br.ufes.util.ResponseSearch;
import br.ufes.validate.ItemBacklogProjetoValidate;
import br.ufes.validate.ProjetoUsuarioValidate;

@Component
public class ItemBacklogProjetoFacade {

	@Autowired
	private ItemBacklogProjetoService itemBackLogProjetoService;

	@Autowired
	private ProjetoService projetoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ItemBacklogProjetoValidate itemBacklogProjetoValidate;

	@Autowired
	private ProjetoUsuarioValidate projetoUsuarioValidate;

	@Autowired
	private ModelMapper modelMapper;

	public ItemBacklogProjetoDTO cadastrarItemBacklogProjeto(Long idProjeto,
			ItemBacklogProjetoInsertDTO itemBacklogProjetoInsertDTO) throws Exception {
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);
		itemBacklogProjetoValidate.validateSave(itemBacklogProjetoInsertDTO);
		var itemBacklogProjeto = modelMapper.map(itemBacklogProjetoInsertDTO, ItemBacklogProjeto.class);

		var projeto = projetoService.getById(idProjeto);
		var codigoNovoItem = itemBackLogProjetoService.obterCodigoNovoItem(idProjeto,
				itemBacklogProjetoInsertDTO.getCategoria());
		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();
		var prioridadeNovoItem = itemBackLogProjetoService.obterNumeroPrioridadeNovoItem(idProjeto);

		itemBacklogProjeto.setPrioridade(prioridadeNovoItem);
		itemBacklogProjeto.setCodigo(codigoNovoItem);
		itemBacklogProjeto.setDataCriacao(LocalDateTime.now());
		itemBacklogProjeto.setAutor(usuarioAutenticado);
		itemBacklogProjeto.setProjeto(projeto);
		if (ObjectUtils.isEmpty(itemBacklogProjetoInsertDTO.getSituacao())) {
			itemBacklogProjeto.setSituacao(SituacaoItemProjetoEnum.REDIGINDO);
		}
		itemBacklogProjeto = itemBackLogProjetoService.save(itemBacklogProjeto);

		return modelMapper.map(itemBacklogProjeto, ItemBacklogProjetoDTO.class);
	}

	public ItemBacklogProjetoDTO atualizarItemBacklogProjeto(Long idItemBacklogProjeto,
			ItemBacklogProjetoUpdateDTO itemBacklogProjetoUpsertDTO) throws Exception {
		var itemBacklogProjeto = itemBackLogProjetoService.getById(idItemBacklogProjeto);

		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(itemBacklogProjeto.getProjeto().getId());
		itemBacklogProjetoValidate.validateSave(itemBacklogProjetoUpsertDTO);

		itemBacklogProjeto.atualizarAtributos(itemBacklogProjetoUpsertDTO);

		itemBacklogProjeto = itemBackLogProjetoService.save(itemBacklogProjeto);
		return modelMapper.map(itemBacklogProjeto, ItemBacklogProjetoDTO.class);
	}

	public ResponseSearch<ItemBacklogProjetoBasicDTO> search(Long idProjeto, ItemBacklogProjetoFilterDTO filterDTO)
			throws Exception {

		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(idProjeto);

		filterDTO.setIdProjeto(idProjeto);

		return itemBackLogProjetoService.search(filterDTO);
	}

	public void deleteItemBacklogProjeto(Long idItemBacklogProjeto) throws Exception {
		var itemBacklogProjeto = itemBackLogProjetoService.getById(idItemBacklogProjeto);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(itemBacklogProjeto.getProjeto().getId());

		var usuarioAutenticado = usuarioService.getUsuarioAutenticado();

		if (!PerfilUsuarioEnum.PRODUCT_OWNER.equals(usuarioAutenticado.getPerfil())
				&& usuarioAutenticado.equals(itemBacklogProjeto.getAutor())) {

			throw new BusinessException("O usuário '" + usuarioAutenticado.getNomeUsuario()
					+ "' possui permissão para remover apenas os itens de backlog de projeto de sua autoria");
		}
		
//		TODO: Não deixar remover caso tenha itens backlog da sprint associado.
		itemBackLogProjetoService.remover(idItemBacklogProjeto);
	}

	public ItemBacklogProjetoDTO getById(Long idItemBacklogProjeto) {
		var itemBackLogProjeto = itemBackLogProjetoService.getById(idItemBacklogProjeto);
		return modelMapper.map(itemBackLogProjeto, ItemBacklogProjetoDTO.class);
	}

	public void repriorizarItemBacklogProjeto(Long idItemBacklogProjeto, Long valorPrioridade) {
		var itemBacklogProjeto = itemBackLogProjetoService.getById(idItemBacklogProjeto);
		projetoUsuarioValidate.validarAcessoUsuarioAutenticadoAoProjeto(itemBacklogProjeto.getProjeto().getId());
		itemBackLogProjetoService.repriorizarItemBacklogProjeto(itemBacklogProjeto, valorPrioridade);
	}

}
