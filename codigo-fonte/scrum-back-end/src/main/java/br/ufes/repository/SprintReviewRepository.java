package br.ufes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufes.entity.SprintReview;

@Repository
public interface SprintReviewRepository extends JpaRepository<SprintReview, Long> {

}
