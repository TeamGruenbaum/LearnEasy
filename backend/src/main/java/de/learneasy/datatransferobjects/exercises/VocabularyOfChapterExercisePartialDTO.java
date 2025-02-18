package de.learneasy.datatransferobjects.exercises;

import de.learneasy.datatransferobjects.exercises.exercisetemplates.VocabularyOfChapterExerciseTemplatePartialDTO;
import de.learneasy.model.exercises.vocabularyofchapter.VocabularyOfChapterExercise;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Getter
public final class VocabularyOfChapterExercisePartialDTO extends ExercisePartialDTO {
    private final @NotNull VocabularyOfChapterExerciseTemplatePartialDTO vocabularyOfChapterExerciseTemplate;

    public VocabularyOfChapterExercisePartialDTO(@NotNull UUID id, @NotNull VocabularyOfChapterExerciseTemplatePartialDTO vocabularyOfChapterExerciseTemplate) {
        super(id);
        this.vocabularyOfChapterExerciseTemplate = vocabularyOfChapterExerciseTemplate;
    }

    public static @NotNull VocabularyOfChapterExercisePartialDTO fromVocabularyOfChapterExercise(VocabularyOfChapterExercise vocabularyOfChapterExercise) {
        return new VocabularyOfChapterExercisePartialDTO(
            vocabularyOfChapterExercise.getId(),
            new VocabularyOfChapterExerciseTemplatePartialDTO(
                vocabularyOfChapterExercise.getVocabularyOfChapterExerciseTemplate().getId(),
                vocabularyOfChapterExercise.getVocabularyOfChapterExerciseTemplate().getName(),
                vocabularyOfChapterExercise.getVocabularyOfChapterExerciseTemplate().getChapter()
            )
        );
    }
}
