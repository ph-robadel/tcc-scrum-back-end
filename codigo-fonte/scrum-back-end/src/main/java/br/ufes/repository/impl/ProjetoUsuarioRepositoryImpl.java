package br.ufes.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.ProjetoUsuarioFilterDTO;
import br.ufes.entity.ProjetoUsuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProjetoUsuarioRepositoryImpl {

	@PersistenceContext
	private EntityManager manager;

	public List<UsuarioResponseDTO> search(ProjetoUsuarioFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT new br.ufes.dto.UsuarioResponseDTO (");
		sqlBuider.append("   u.id, ");
		sqlBuider.append("   u.nomeCompleto, ");
		sqlBuider.append("   u.nomeUsuario, ");
		sqlBuider.append("   u.email, ");
		sqlBuider.append("   u.ativo, ");
		sqlBuider.append("   u.perfil");
		sqlBuider.append(" ) ");

		searchFrom(filterDTO, sqlBuider, params);

		if (!ObjectUtils.isEmpty(filterDTO.getFieldSort()) && !ObjectUtils.isEmpty(filterDTO.getSortOrder())) {
			sqlBuider.append(" ORDER BY u." + filterDTO.getFieldSort() + " " + filterDTO.getSortOrder().name());
		}

		var query = manager.createQuery(sqlBuider.toString(), UsuarioResponseDTO.class);
		params.forEach(query::setParameter);

		query.setFirstResult(filterDTO.getPage() * filterDTO.getSize());
		query.setMaxResults(filterDTO.getSize());
		return query.getResultList();
	}

	public Long searchCount(ProjetoUsuarioFilterDTO filterDTO) {

		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT count(1) ");
		searchFrom(filterDTO, sqlBuider, params);

		var query = manager.createQuery(sqlBuider.toString(), Long.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();
		return !ObjectUtils.isEmpty(result) ? result.get(0) : 0l;
	}

	private void searchFrom(ProjetoUsuarioFilterDTO filterDTO, StringBuilder sqlBuider,
			HashMap<String, Object> params) {
		sqlBuider.append(" FROM ");
		sqlBuider.append("   ProjetoUsuario pu ");
		sqlBuider.append("   join pu.usuario u");
		sqlBuider.append("   join pu.projeto p");
		sqlBuider.append(" WHERE 1=1 ");

		if (!ObjectUtils.isEmpty(filterDTO.getIdProjeto())) {
			sqlBuider.append(" and p.id = :idProjeto ");
			params.put("idProjeto", filterDTO.getIdProjeto() );
		}

		if (!ObjectUtils.isEmpty(filterDTO.getNomeUsuario())) {
			sqlBuider.append(" and u.nomeUsuario ilike :nome ");
			params.put("nome", "%" + filterDTO.getNomeUsuario() + "%");
		}

		if (Boolean.TRUE.equals(filterDTO.getApenasAtivo())) {
			sqlBuider.append(" and u.ativo = true");
		}

		if (!ObjectUtils.isEmpty(filterDTO.getPerfil())) {
			sqlBuider.append(" and u.perfil = :perfil");
			params.put("perfil", filterDTO.getPerfil());
		}
	}
	
	public ProjetoUsuario getByIdProjetoAndIdUsuario(Long idProjeto, Long idUsuario) {
		var sqlBuider = new StringBuilder();
		var params = new HashMap<String, Object>();

		sqlBuider.append(" SELECT pu ");
		sqlBuider.append(" FROM ProjetoUsuario pu ");
		sqlBuider.append("   JOIN pu.projeto p ");
		sqlBuider.append("   JOIN pu.usuario u ");
		sqlBuider.append(" WHERE p.id = :idProjeto ");
		sqlBuider.append("       and u.id = :idUsuario ");

		params.put("idProjeto", idProjeto);
		params.put("idUsuario", idUsuario);

		var query = manager.createQuery(sqlBuider.toString(), ProjetoUsuario.class);
		params.forEach(query::setParameter);

		var result = query.getResultList();

		return !ObjectUtils.isEmpty(result) ? result.get(0) : null;
	}

}
