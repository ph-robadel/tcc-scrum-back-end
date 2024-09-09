package br.ufes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.entity.SprintRetrospective;

@Repository
public interface SprintRetrospectiveRepository extends JpaRepository<SprintRetrospective, Long> {

}
