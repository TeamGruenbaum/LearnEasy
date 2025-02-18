package de.learneasy.model.exercises.vocabularyofchapter;

import de.learneasy.model.Presentation;
import de.learneasy.model.exercises.Exercise;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(callSuper = true)
public class VocabularyOfChapterExercise extends Exercise
{
    @ManyToOne
    private @NotNull VocabularyOfChapterExerciseTemplate vocabularyOfChapterExerciseTemplate;

    private @NotNull Presentation presentation = Presentation.TextAskForeignLanguage;

    @ElementCollection
    private @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers = null;

    private int numberOfCorrectAnswersForCompletion = 10;

    public VocabularyOfChapterExercise(
            final @NotNull VocabularyOfChapterExerciseTemplate vocabularyOfChapterExerciseTemplate,
            final @Nullable Presentation presentation,
            final @Nullable List<@NotNull Integer> selectedVocabularyWordNumbers,
            final @Nullable Integer numberOfCorrectAnswersForCompletion
    ) {
        this.vocabularyOfChapterExerciseTemplate = vocabularyOfChapterExerciseTemplate;
        if(presentation != null) this.presentation = presentation;
        if(selectedVocabularyWordNumbers != null) {
            if(selectedVocabularyWordNumbers.isEmpty()) throw new IllegalArgumentException("Passed selected vocabulary word numbers are empty.");
            if(selectedVocabularyWordNumbers.stream().filter(number -> number < 1).toList().size() > 0) throw new IllegalArgumentException("Passed selected vocabulary word numbers contain a number less than 1.");
            if(selectedVocabularyWordNumbers.stream().distinct().toList().size() != selectedVocabularyWordNumbers.size()) throw new IllegalArgumentException("Passed selected vocabulary word numbers contain duplicates.");
            this.selectedVocabularyWordNumbers = selectedVocabularyWordNumbers;
        }
        if(numberOfCorrectAnswersForCompletion != null) this.numberOfCorrectAnswersForCompletion = numberOfCorrectAnswersForCompletion;
    }
}