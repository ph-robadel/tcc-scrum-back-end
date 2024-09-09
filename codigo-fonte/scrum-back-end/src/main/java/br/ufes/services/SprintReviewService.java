package br.ufes.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemReviewDTO;
import br.ufes.dto.SprintReviewDTO;
import br.ufes.entity.ItemBacklogProjeto;
import br.ufes.entity.ItemReview;
import br.ufes.entity.SprintReview;
import br.ufes.repository.SprintReviewRepository;

@Service
public class SprintReviewService extends EventoService {

	@Autowired
	private SprintReviewRepository sprintReviewRepository;

	public SprintReview save(SprintReview sprintReview) {
		return sprintReviewRepository.save(sprintReview);
	}

	public void atualizarItensReview(SprintReviewDTO reviewDTO, List<ItemBacklogProjeto> itensSprint,
			SprintReview sprintReview) {

		if (sprintReview.getItensAprovados() == null) {
			sprintReview.setItensAprovados(new ArrayList<>());
		}

		if (sprintReview.getItensRejeitados() == null) {
			sprintReview.setItensRejeitados(new ArrayList<>());
		}

		if (sprintReview.getItensAprovadosParcialmente() == null) {
			sprintReview.setItensAprovadosParcialmente(new ArrayList<>());
		}

		updateItemReview(itensSprint, sprintReview.getItensAprovados(), reviewDTO.getItensAprovados());
		updateItemReview(itensSprint, sprintReview.getItensRejeitados(), reviewDTO.getItensRejeitados());
		updateItemReview(itensSprint, sprintReview.getItensAprovadosParcialmente(),
				reviewDTO.getItensAprovadosParcialmente());

	}

	private void updateItemReview(List<ItemBacklogProjeto> itensSprint, List<ItemReview> itensReview,
			List<ItemReviewDTO> itensReviewDTO) {
		itensReview.clear();
		if (!ObjectUtils.isEmpty(itensReviewDTO)) {
			itensReviewDTO.stream().forEach(itemDTO -> {
				var itemReview = new ItemReview();
				var item = itensSprint.stream()
						.filter(ibp -> ibp.getId().equals(itemDTO.getIdItemBacklogProjeto())).findFirst();
				itemReview.setDescricao(itemDTO.getDescricao());
				itemReview.setItemBacklogProjeto(item.get());
				itensReview.add(itemReview);
			});
		}
	}

}
