package de.learneasy.repositories;

import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExerciseTemplatesRepository extends JpaRepository<ExerciseTemplate, UUID> {}
