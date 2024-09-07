package br.ufes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.entity.SprintPlanning;

@Repository
public interface SprintPlanningRepository extends JpaRepository<SprintPlanning, Long> {


}
