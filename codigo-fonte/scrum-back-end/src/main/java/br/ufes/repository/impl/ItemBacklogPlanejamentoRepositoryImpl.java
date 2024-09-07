package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoSimpleDTO;
import br.ufes.dto.filter.ItemBacklogPlanejamentoFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ItemBacklogPlanejamentoRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<ItemBacklogProjetoSimpleDTO> search(ItemBacklogPlanejamentoFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.ItemBacklogProjetoSimpleDTO (");
		sqlBuider.append("   ibp.id, ");
		sqlBuider.append("   ibp.titulo, ");
		sqlBuider.append("   ibp.codigo, ");
		sqlBuider.append("   ibp.categoria, ");
		sqlBuider.append("   ibp.situacao ");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY " + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), ItemBacklogProjetoSimpleDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(ItemBacklogPlanejamentoFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(ItemBacklogPlanejamentoFilterDTO filterDTO, StringBuilder sqlBuider,
			HashMap<String, Object> params) {
		sqlBuider.append(" FROM ItemBacklogPlanejamento ibpl ");
		sqlBuider.append("      join ibpl.itemBacklogProjeto ibp ");
		sqlBuider.append("      join ibpl.sprint s ");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getTitulo())) {
			sqlBuider.append(" and ibp.titulo ilike :titulo ");
			params.put("titulo", "%" + filterDTO.getTitulo() + "%");
		}

		if (!ObjectUtils.isEmpty(filterDTO.getIdSprint())) {
			sqlBuider.append(" and s.id = :idSprint ");
			params.put("idSprint", filterDTO.getIdSprint());
		}

	}
}
