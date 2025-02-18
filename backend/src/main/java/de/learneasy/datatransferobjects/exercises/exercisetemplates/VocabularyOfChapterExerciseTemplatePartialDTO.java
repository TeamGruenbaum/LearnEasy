package de.learneasy.datatransferobjects.exercises.exercisetemplates;

import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Getter
public final class VocabularyOfChapterExerciseTemplatePartialDTO extends ExerciseTemplatePartialDTO {
    public VocabularyOfChapterExerciseTemplatePartialDTO(@NotNull UUID id, @NotNull String name, @NotNull String chapter) {
        super(id, name, chapter);
    }

    public static @NotNull VocabularyOfChapterExerciseTemplatePartialDTO fromVocabularyOfChapterExerciseTemplate(VocabularyOfChapterExerciseTemplate vocabularyOfChapterExerciseTemplate) {
        return new VocabularyOfChapterExerciseTemplatePartialDTO(
            vocabularyOfChapterExerciseTemplate.getId(),
            vocabularyOfChapterExerciseTemplate.getName(),
            vocabularyOfChapterExerciseTemplate.getChapter()
        );
    }
}
