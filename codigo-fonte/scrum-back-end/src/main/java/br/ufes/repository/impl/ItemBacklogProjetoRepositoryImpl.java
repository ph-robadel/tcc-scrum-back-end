package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.ItemBacklogProjetoBasicDTO;
import br.ufes.dto.filter.ItemBacklogProjetoFilterDTO;
import br.ufes.enums.CategoriaItemProjetoEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ItemBacklogProjetoRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<ItemBacklogProjetoBasicDTO> search(ItemBacklogProjetoFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.ItemBacklogProjetoBasicDTO (");
		sqlBuider.append("   ibp.id, ");
		sqlBuider.append("   ibp.titulo, ");
		sqlBuider.append("   ibp.codigo, ");
		sqlBuider.append("   ibp.categoria, ");
		sqlBuider.append("   ibp.situacao, ");
		sqlBuider.append("   ibp.prioridade ");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY ibp." + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), ItemBacklogProjetoBasicDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(ItemBacklogProjetoFilterDTO filterDTO) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(ItemBacklogProjetoFilterDTO filterDTO, StringBuilder sqlBuider,
			HashMap<String, Object> params) {
		sqlBuider.append(" FROM ItemBacklogProjeto ibp ");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getIdProjeto())) {
			sqlBuider.append(" and ibp.projeto.id = :idProjeto ");
			params.put("idProjeto", filterDTO.getIdProjeto());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getTitulo())) {
			sqlBuider.append(" and ibp.titulo ilike :titulo ");
			params.put("titulo", "%" + filterDTO.getTitulo() + "%");
		}

		if (!ObjectUtils.isEmpty(filterDTO.getCodigo())) {
			sqlBuider.append(" and ibp.codigo = :codigo ");
			params.put("codigo", filterDTO.getCodigo());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getCategoria())) {
			sqlBuider.append(" and ibp.categoria = :categoria ");
			params.put("categoria", filterDTO.getCategoria());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getSituacao())) {
			sqlBuider.append(" and ibp.situacao = :situacao ");
			params.put("situacao", filterDTO.getSituacao());
		}

		if (!ObjectUtils.isEmpty(filterDTO.getIdAutor())) {
			sqlBuider.append(" and ibp.autor.id = :idAutor ");
			params.put("idAutor", filterDTO.getIdAutor());
		}

	}

	public Long obterCodigoNovoItem(Long idProjeto, CategoriaItemProjetoEnum categoria) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT max(ibp.codigo) ");
		sqlBuider.append(" FROM ItemBacklogProjeto ibp ");
		sqlBuider.append(" WHERE ibp.projeto.id = :idProjeto ");
		sqlBuider.append("       and ibp.categoria = :categoria ");

		params.put("idProjeto", idProjeto);
		params.put("categoria", categoria);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();

		return !ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get(0)) ? result.get(0) + 1l : 1l;
	}

	public Long obterNumeroPrioridadeNovoItem(Long idProjeto) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT max(ibp.prioridade) ");
		sqlBuider.append(" FROM ItemBacklogProjeto ibp ");
		sqlBuider.append(" WHERE ibp.projeto.id = :idProjeto ");

		params.put("idProjeto", idProjeto);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();

		return !ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get(0)) ? result.get(0) + 1l : 1l;
	}

	public void aumentarPrioridadeItem(Long idItemBacklogProjeto, Long antigaPrioridade, Long novaPrioridade) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" update ");
		hqlBuilder.append(" 	ItemBacklogProjeto ibp ");
		hqlBuilder.append(" set ");
		hqlBuilder.append(" 	ibp.prioridade = ibp.prioridade + 1 ");
		hqlBuilder.append(" where ");
		hqlBuilder.append(" 	ibp.prioridade < :antiga ");
		hqlBuilder.append(" 	and prioridade >= :nova ");
		hqlBuilder.append(" 	and ibp.projeto.id = ( ");
		hqlBuilder.append(" 	select ");
		hqlBuilder.append(" 		ibpSub.projeto.id ");
		hqlBuilder.append(" 	from ");
		hqlBuilder.append(" 		ItemBacklogProjeto ibpSub ");
		hqlBuilder.append(" 	where ");
		hqlBuilder.append(" 		ibpSub.id = :idItemBacklogProjeto) ");
		executeUpdatePrioridade(idItemBacklogProjeto, antigaPrioridade, novaPrioridade, hqlBuilder.toString());
		updateItem(idItemBacklogProjeto, novaPrioridade);
	}

	public void diminuirPrioridadeItem(Long idItemBacklogProjeto, Long antigaPrioridade, Long novaPrioridade) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" update ");
		hqlBuilder.append(" 	ItemBacklogProjeto ibp ");
		hqlBuilder.append(" set ");
		hqlBuilder.append(" 	ibp.prioridade = ibp.prioridade - 1 ");
		hqlBuilder.append(" where ");
		hqlBuilder.append(" 	ibp.prioridade > :antiga ");
		hqlBuilder.append(" 	and prioridade <= :nova ");
		hqlBuilder.append(" 	and ibp.projeto.id = ( ");
		hqlBuilder.append(" 	select ");
		hqlBuilder.append(" 		ibpSub.projeto.id ");
		hqlBuilder.append(" 	from ");
		hqlBuilder.append(" 		ItemBacklogProjeto ibpSub ");
		hqlBuilder.append(" 	where ");
		hqlBuilder.append(" 		ibpSub.id = :idItemBacklogProjeto); ");
		executeUpdatePrioridade(idItemBacklogProjeto, antigaPrioridade, novaPrioridade, hqlBuilder.toString());
		updateItem(idItemBacklogProjeto, novaPrioridade);
	}
	
	public void repriorizarDeleteItem(Long idItemBacklogProjeto) {
		var hqlBuilder = new StringBuilder();
		hqlBuilder.append(" UPDATE item_backlog_projeto " );
		hqlBuilder.append(" SET prioridade = prioridade - 1 " );
		hqlBuilder.append(" WHERE id_projeto = (" );
		hqlBuilder.append("     SELECT ibpSub.id_projeto " );
		hqlBuilder.append("     FROM item_backlog_projeto ibpSub " );
		hqlBuilder.append("     WHERE ibpSub.id_item_backlog_projeto = :idItemBacklogProjeto " );
		hqlBuilder.append(" ) " );
		hqlBuilder.append(" AND prioridade > (" );
		hqlBuilder.append("     SELECT ibpSub.prioridade " );
		hqlBuilder.append("     FROM item_backlog_projeto ibpSub " );
		hqlBuilder.append("     WHERE ibpSub.id_item_backlog_projeto = :idItemBacklogProjeto " );
		hqlBuilder.append(" )" );
		var updatePrioridadeQuery = manager.createQuery(hqlBuilder.toString());
		updatePrioridadeQuery.setParameter("idItemBacklogProjeto", idItemBacklogProjeto);
		updatePrioridadeQuery.executeUpdate();
	}

	private void executeUpdatePrioridade(Long idItemBacklogProjeto, Long antigaPrioridade, Long novaPrioridade, String hql) {
		var query = manager.createQuery(hql);
		query.setParameter("idItemBacklogProjeto", idItemBacklogProjeto);
		query.setParameter("antiga", antigaPrioridade);
		query.setParameter("nova", novaPrioridade);
		query.executeUpdate();
	}

	private void updateItem(Long idItemBacklogProjeto, Long novaPrioridade) {
		var updatePrioridadeItem = " UPDATE ItemBacklogProjeto ibp SET ibp.prioridade = :nova WHERE ibp.id = :idItem ";
		var updatePrioridadeQuery = manager.createQuery(updatePrioridadeItem);
		updatePrioridadeQuery.setParameter("idItem", idItemBacklogProjeto);
		updatePrioridadeQuery.setParameter("nova", novaPrioridade);
		updatePrioridadeQuery.executeUpdate();
	}
}
