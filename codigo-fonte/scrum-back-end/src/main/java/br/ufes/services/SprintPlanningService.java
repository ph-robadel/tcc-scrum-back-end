package br.ufes.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintPlanningDTO;
import br.ufes.entity.CapacidadeUsuario;
import br.ufes.entity.ItemBacklogPlanning;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.entity.SprintPlanning;
import br.ufes.entity.Usuario;
import br.ufes.repository.SprintPlanningRepository;

@Service
public class SprintPlanningService extends EventoService {

	@Autowired
	private SprintPlanningRepository sprintPlanningRepository;

	public SprintPlanning save(SprintPlanning sprintPlanning) {
		return sprintPlanningRepository.save(sprintPlanning);
	}

	public void atualizarItensSelecionadosPlanning(SprintPlanningDTO planningDTO, List<Usuario> timeProjeto,
			List<ItemBacklogProjeto> itensBacklogProjeto, SprintPlanning sprintPlanning) {

		if (sprintPlanning.getItensSelecionados() == null) {
			sprintPlanning.setItensSelecionados(new ArrayList<>());
		} else {
			sprintPlanning.getItensSelecionados().clear();
		}
		if (!ObjectUtils.isEmpty(planningDTO.getItensSelecionados())) {
			planningDTO.getItensSelecionados().stream().forEach(itemDTO -> {
				var itemPlanning = new ItemBacklogPlanning();
				var responsavelRealizacao = timeProjeto.stream()
						.filter(u -> u.getId().equals(itemDTO.getIdResponsavelRealizacao())).findFirst();
				var item = itensBacklogProjeto.stream()
						.filter(ibp -> ibp.getId().equals(itemDTO.getIdItemBacklogProjeto())).findFirst();
				itemPlanning.setDescricao(itemDTO.getDescricao());
				itemPlanning.setHorasEstimadas(itemDTO.getHorasEstimadas());
				itemPlanning.setSprintPlanning(sprintPlanning);
				itemPlanning.setItemBacklogProjeto(item.get());
				itemPlanning.setResponsavelRealizacao(responsavelRealizacao.get());
				sprintPlanning.getItensSelecionados().add(itemPlanning);
			});

		}
	}

	public void atualizarCapacidadePlanning(SprintPlanningDTO planningDTO, List<Usuario> timeProjeto,
			SprintPlanning sprintPlanning) {

		if (sprintPlanning.getCapacidadeTime() == null) {
			sprintPlanning.setCapacidadeTime(new ArrayList<>());
		} else {
			sprintPlanning.getCapacidadeTime().clear();
		}
		if (!ObjectUtils.isEmpty(planningDTO.getCapacidadeTime())) {
			planningDTO.getCapacidadeTime().stream().forEach(capacidadeDTO -> {
				var capacidadeUsuario = new CapacidadeUsuario();
				var usuario = timeProjeto.stream().filter(u -> u.getId().equals(capacidadeDTO.getIdUsuario()))
						.findFirst();
				capacidadeUsuario.setHoras(capacidadeDTO.getHoras());
				capacidadeUsuario.setJustificativa(capacidadeDTO.getJustificativa());
				capacidadeUsuario.setUsuario(usuario.get());
				capacidadeUsuario.setSprintPlanning(sprintPlanning);
				sprintPlanning.getCapacidadeTime().add(capacidadeUsuario);
			});
		}
	}

}
