package br.ufes.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.SprintDailyBasicDTO;
import br.ufes.dto.SprintDailyDTO;
import br.ufes.dto.filter.SprintDailyFilterDTO;
import br.ufes.entity.PerguntaRespostaDaily;
import br.ufes.entity.RegistroDaily;
import br.ufes.entity.SprintDaily;
import br.ufes.entity.Usuario;
import br.ufes.repository.SprintDailyRepository;
import br.ufes.util.ResponseSearch;

@Service
public class SprintDailyService extends EventoService {

	@Autowired
	private SprintDailyRepository sprintDailyRepository;

	public SprintDaily save(SprintDaily sprintDaily) {
		return sprintDailyRepository.save(sprintDaily);
	}

	public void atualizarRegistroDaily(SprintDailyDTO dailyDTO, List<Usuario> timeProjeto, SprintDaily sprintDaily) {

		if (!ObjectUtils.isEmpty(dailyDTO.getRegistroDaily())) {
			dailyDTO.getRegistroDaily().stream().forEach(registroDTO -> {
				var registroDaily = new RegistroDaily();
				var usuario = timeProjeto.stream().filter(u -> u.getId().equals(registroDTO.getIdUsuario()))
						.findFirst();
				registroDaily.setUsuario(usuario.get());
				registroDaily.setSprintDaily(sprintDaily);
				if (!ObjectUtils.isEmpty(registroDTO.getPerguntaResposta())) {
					if (ObjectUtils.isEmpty(registroDaily.getPerguntaResposta())) {
						registroDaily.setPerguntaResposta(new ArrayList<>());
					} else {
						registroDaily.getPerguntaResposta().clear();
					}
					registroDTO.getPerguntaResposta().forEach(dto -> {
						var perguntaResposta = new PerguntaRespostaDaily(dto.getPergunta(), dto.getResposta(), registroDaily);
						registroDaily.getPerguntaResposta().add(perguntaResposta);
					});
				}
				sprintDaily.getRegistroDaily().add(registroDaily);
			});
		}
	}

	public ResponseSearch<SprintDailyBasicDTO> search(SprintDailyFilterDTO filterDTO) {
		List<SprintDailyBasicDTO> listPage = sprintDailyRepository.search(filterDTO);
		Long total = sprintDailyRepository.searchCount(filterDTO);

		return new ResponseSearch<>(listPage, total);
	}

}
