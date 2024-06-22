package br.ufes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

//	public UserDetails findByNomeUsuario(String nomeUsuario);
	public Usuario findByNomeUsuario(String nomeUsuario);

}
