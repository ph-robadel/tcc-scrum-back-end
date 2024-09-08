package br.ufes.validate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintDailyDTO;
import br.ufes.entity.ProjetoUsuario;
import br.ufes.entity.Sprint;
import br.ufes.entity.SprintDaily;
import br.ufes.entity.Usuario;
import br.ufes.exception.BusinessException;
import br.ufes.services.SprintService;

@Component
public class SprintDailyValidate {

	@Autowired
	public SprintService sprintService;

	@Autowired
	public EventoValidate eventoValidate;

	public void validateSaveDaily(Sprint sprint, SprintDailyDTO dailyDTO, boolean isUpdate) {
		List<String> erros = new ArrayList<>();
		var formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

		List<Usuario> timeProjeto = sprint.getProjeto().getTime().stream()
				.filter(projUsr -> projUsr.isAtivo() && projUsr.getUsuario().isAtivo()).map(ProjetoUsuario::getUsuario)
				.collect(Collectors.toList());

		List<SprintDaily> dailys = sprint.getDailys();
		if (!ObjectUtils.isEmpty(dailys) && dailys.stream()
				.anyMatch(daily -> !daily.getId().equals(dailyDTO.getId()) && !ObjectUtils.isEmpty(dailyDTO.getInicio())
						&& daily.getInicio().toLocalDate().equals(dailyDTO.getInicio().toLocalDate()))) {
			throw new BusinessException("Há outra daily registrada para o dia "
					+ dailyDTO.getInicio().toLocalDate().format(formatter));
		}

		if (isUpdate) {
			validarDailyEncontrada(sprint, dailyDTO.getId());
		}
		
		var dataInicio = !ObjectUtils.isEmpty(dailyDTO.getInicio()) ? dailyDTO.getInicio().toLocalDate() : null;
		var dataFim = !ObjectUtils.isEmpty(dailyDTO.getFim()) ? dailyDTO.getFim().toLocalDate() : null;
		if(!ObjectUtils.isEmpty(dataInicio) ) {
			if(dataInicio.isBefore(sprint.getDataInicio()) || dataInicio.isAfter(sprint.getDataFim())) {
				erros.add("A data " + dataInicio.format(formatter) + " não pertence ao intervalo de datas da sprint");
			}
			
			if(!ObjectUtils.isEmpty(dataFim) && !dataFim.isEqual(dataInicio)) {
				erros.add("O início e fim da daily devem ser no mesmo dia");
			}
		}

		var listaRegistroDailyDTO = dailyDTO.getRegistroDaily();
		var listaIdsParticipantesDTO = dailyDTO.getIdsParticipantes();

		eventoValidate.validateSituacaoSprint(sprint);
		eventoValidate.validateCamposEvento(dailyDTO, erros);
		eventoValidate.validateParticipantesEvento(erros, timeProjeto, listaIdsParticipantesDTO);

		if (!ObjectUtils.isEmpty(listaRegistroDailyDTO)) {
			var possuiRegistroInvalida = listaRegistroDailyDTO.stream()
					.anyMatch(registro -> ObjectUtils.isEmpty(registro.getIdUsuario()));
			if (possuiRegistroInvalida) {
				erros.add("O usuário deve ser informado em todos os registros da daily");
			} else {
				var listaUsuariosInvalidos = listaRegistroDailyDTO.stream()
						.filter(registroDTO -> timeProjeto.stream()
								.noneMatch(u -> u.getId().equals(registroDTO.getIdUsuario())))
						.map(registroDTO -> registroDTO.getIdUsuario().toString()).collect(Collectors.toList());
				if (listaUsuariosInvalidos.size() == 1) {
					erros.add("Registro daily: O usuário " + listaUsuariosInvalidos.get(0)
							+ " não é membro ativo do projeto");
				} else if (listaUsuariosInvalidos.size() > 1) {
					erros.add("Registro daily: Os Usuários "
							+ listaUsuariosInvalidos.stream().collect(Collectors.joining(", "))
							+ " não são membros ativos do projeto");
				}
			}
		}

		if (!ObjectUtils.isEmpty(erros)) {
			throw new BusinessException(erros);
		}
	}

	public void validarDailyEncontrada(Sprint sprint, Long idDaily) {
		if (ObjectUtils.isEmpty(sprint.getDailys())
				|| sprint.getDailys().stream().noneMatch(daily -> daily.getId().equals(idDaily))) {
			throw new BusinessException("Daily não encontrada na sprint");
		}
	}

}
