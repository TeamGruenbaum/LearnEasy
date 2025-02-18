package de.learneasy.repositories;

import de.learneasy.model.exercises.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExercisesRepository extends JpaRepository<Exercise, UUID> {}
