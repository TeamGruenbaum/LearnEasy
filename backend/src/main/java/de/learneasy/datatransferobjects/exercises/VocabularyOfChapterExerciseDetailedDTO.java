package de.learneasy.datatransferobjects.exercises;

import de.learneasy.datatransferobjects.exercises.exercisetemplates.VocabularyOfChapterExerciseTemplateDetailedDTO;
import de.learneasy.datatransferobjects.PresentationDTO;
import de.learneasy.model.exercises.vocabularyofchapter.VocabularyOfChapterExercise;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
public final class VocabularyOfChapterExerciseDetailedDTO extends ExerciseDetailedDTO {
    private final @NotNull VocabularyOfChapterExerciseTemplateDetailedDTO vocabularyOfChapterExerciseTemplate;
    private final @NotNull PresentationDTO presentation;
    private final @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers;
    private final @NotNull Integer numberOfCorrectAnswersForCompletion;

    public VocabularyOfChapterExerciseDetailedDTO(
            final @NotNull UUID id,
            final @NotNull VocabularyOfChapterExerciseTemplateDetailedDTO vocabularyOfChapterExerciseTemplate,
            final @NotNull PresentationDTO presentation,
            final @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers,
            final @NotNull Integer numberOfCorrectAnswersForCompletion
    ) {
        super(id);
        this.vocabularyOfChapterExerciseTemplate = vocabularyOfChapterExerciseTemplate;
        this.presentation = presentation;
        this.selectedVocabularyWordNumbers = selectedVocabularyWordNumbers;
        this.numberOfCorrectAnswersForCompletion = numberOfCorrectAnswersForCompletion;
    }

    public static @NotNull VocabularyOfChapterExerciseDetailedDTO fromVocabularyOfChapterExercise(VocabularyOfChapterExercise vocabularyOfChapterExercise) {
        return new VocabularyOfChapterExerciseDetailedDTO(
            vocabularyOfChapterExercise.getId(),
            VocabularyOfChapterExerciseTemplateDetailedDTO.fromVocabularyOfChapterExerciseTemplate(vocabularyOfChapterExercise.getVocabularyOfChapterExerciseTemplate()),
            PresentationDTO.fromPresentation(vocabularyOfChapterExercise.getPresentation()),
            vocabularyOfChapterExercise.getSelectedVocabularyWordNumbers(),
            vocabularyOfChapterExercise.getNumberOfCorrectAnswersForCompletion()
        );
    }
}

