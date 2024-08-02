package br.ufes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.dto.UsuarioResponseDTO;
import br.ufes.dto.filter.UsuarioFilterDTO;
import br.ufes.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Usuario findByNomeUsuario(String nomeUsuario);
	
	public boolean existsByNomeUsuario(String nomeUsuario);

	public List<UsuarioResponseDTO> search(UsuarioFilterDTO usuarioFilterDTO);

	public Long searchCount(UsuarioFilterDTO usuarioFilterDTO);
}
