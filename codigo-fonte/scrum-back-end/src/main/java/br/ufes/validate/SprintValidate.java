package br.ufes.validate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintUpsertDTO;
import br.ufes.entity.Projeto;
import br.ufes.entity.Sprint;
import br.ufes.enums.EventoFinalizacaoProjetoEnum;
import br.ufes.enums.SituacaoSprintEnum;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;
import br.ufes.util.DateUtils;

@Component
public class SprintValidate {

	@Autowired
	public SprintService sprintService;

	public void validateSave(Projeto projeto, SprintUpsertDTO projetoInsertDTO, Integer numeroUpdate) {
		List<String> erros = new ArrayList<>();

		if (ObjectUtils.isEmpty(projetoInsertDTO.getDataInicio())) {
			erros.add("Informe a data inicial da sprint");
		} else {
			var dataInicial = projetoInsertDTO.getDataInicio();
			var dataFinal = DateUtils.adicionarDiasUteis(dataInicial, projeto.getQuantidadeDiasSprint());
			var sprintsIntervaloData = sprintService.obterSprintsIntervaloDatas(projeto.getId(), dataInicial,
					dataFinal);

			if (!ObjectUtils.isEmpty(sprintsIntervaloData)) {
				var dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");

				String numerosSprints = sprintsIntervaloData.stream()
						.filter(sprint -> !sprint.getNumero().equals(numeroUpdate))
						.map(sprint -> sprint.getNumero().toString()).collect(Collectors.joining());
				if (!ObjectUtils.isEmpty(numerosSprints)) {
					erros.add("Não é possível iniciar uma nova sprint na data informada. As datas do intervalo entre "
							+ dataInicial.format(dtf) + " e " + dataFinal.format(dtf) + " estão contidas "
							+ (numerosSprints.length() > 1 ? "nas sprints " : "na sprint ") + numerosSprints);
				}
			}
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}
	
	public void validarFinalizacaoSprint(Sprint sprint) {
		var situacaoSprint = sprint.getSituacao();
		if (SituacaoSprintEnum.CANCELADA.equals(situacaoSprint) || SituacaoSprintEnum.CONCLUIDA.equals(situacaoSprint)) {
			throw new BusinessException("Sprint já está finalizada com a situação " + situacaoSprint.getSituacao());
		}
	}
	
	public void validarConclusaoProjeto(Sprint sprint) {
		List<String> erros = new ArrayList<>();
		
		var eventoFinalizacao = sprint.getProjeto().getEventoFinalizacao();
		if(ObjectUtils.isEmpty(sprint.getPlanning())) {
			erros.add("Planning não cadastrada");
		}
		
		if(ObjectUtils.isEmpty(sprint.getDailys())) {
			erros.add("Daily não cadastrada");
		}
		
		if(ObjectUtils.isEmpty(sprint.getReview())) {
			erros.add("Review não cadastrada");
		}
		
		if(EventoFinalizacaoProjetoEnum.RETROSPECTIVE.equals(eventoFinalizacao)) {
			if(ObjectUtils.isEmpty(sprint.getRetrospective())) {
				erros.add("Retrospective não cadastrada");
			}
		}
		
		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

}
