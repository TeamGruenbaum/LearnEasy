package de.learneasy.datatransferobjects.exercises;

import de.learneasy.datatransferobjects.PresentationDTO;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
public final class VocabularyOfChapterExerciseCreationDTO extends ExerciseCreationDTO {
    private final @Nullable PresentationDTO presentation;
    private final @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers;
    private final @Nullable Integer numberOfCorrectAnswersForCompletion;

    public VocabularyOfChapterExerciseCreationDTO(
        @NotNull UUID exerciseTemplateId,
        @Nullable PresentationDTO presentation,
        @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers,
        @Nullable Integer numberOfCorrectAnswersForCompletion
    ) {
        super(exerciseTemplateId);
        this.presentation = presentation;
        this.selectedVocabularyWordNumbers = selectedVocabularyWordNumbers;
        this.numberOfCorrectAnswersForCompletion = numberOfCorrectAnswersForCompletion;
    }
}
