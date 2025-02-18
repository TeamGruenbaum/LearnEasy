package de.learneasy.repositories;

import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VocabularyWordsRepository extends JpaRepository<VocabularyWord, UUID> {}