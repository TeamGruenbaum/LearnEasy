package de.learneasy.datatransferobjects.exercises.exercisetemplates;

import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
public final class VocabularyOfChapterExerciseTemplateDetailedDTO extends ExerciseTemplateDetailedDTO {
    private final @NotNull List<@NotNull VocabularyWordDTO> vocabulary;

    public VocabularyOfChapterExerciseTemplateDetailedDTO(@NotNull UUID id, @NotNull String name, @NotNull String chapter, @NotNull List<@NotNull VocabularyWordDTO> vocabulary) {
        super(id, name, chapter);
        this.vocabulary = vocabulary;
    }

    public static @NotNull VocabularyOfChapterExerciseTemplateDetailedDTO fromVocabularyOfChapterExerciseTemplate(VocabularyOfChapterExerciseTemplate vocabularyOfChapterExerciseTemplate) {
        return new VocabularyOfChapterExerciseTemplateDetailedDTO(
            vocabularyOfChapterExerciseTemplate.getId(),
            vocabularyOfChapterExerciseTemplate.getName(),
            vocabularyOfChapterExerciseTemplate.getChapter(),
            vocabularyOfChapterExerciseTemplate.getVocabulary().stream().map(VocabularyWordDTO::fromVocabularyWord).toList()
        );
    }
}
