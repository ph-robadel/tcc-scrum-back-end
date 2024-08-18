package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ProjetoBasicDTO;
import br.ufes.dto.filter.ProjetoFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProjetoRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<ProjetoBasicDTO> search(ProjetoFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.ProjetoBasicDTO (");
		sqlBuider.append("   p.id, ");
		sqlBuider.append("   p.nomeCompleto, ");
		sqlBuider.append("   p.descricao ");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY " + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), ProjetoBasicDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(ProjetoFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(ProjetoFilterDTO filterDTO, StringBuilder sqlBuider, HashMap<String, Object> params) {
		sqlBuider.append(" FROM ");
		sqlBuider.append("   ProjetoUsuario pu");
		sqlBuider.append("   join pu.projeto p");
		sqlBuider.append("   join pu.usuario u");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getIdUsuario())) {
			sqlBuider.append(" and u.id = :idUsuario");
			params.put("idUsuario", filterDTO.getIdUsuario());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getNome())) {
			sqlBuider.append(" and p.nome ilike :nome ");
			params.put("nome", "%" + filterDTO.getNome() + "%");
		}

		if (Boolean.TRUE.equals(filterDTO.getApenasAtivo())) {
			sqlBuider.append(" and p.ativo = true");
		}

	}

}
