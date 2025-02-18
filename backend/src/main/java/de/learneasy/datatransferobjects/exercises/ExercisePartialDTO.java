package de.learneasy.datatransferobjects.exercises;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.learneasy.model.exercises.Exercise;
import de.learneasy.model.exercises.vocabularyofchapter.VocabularyOfChapterExercise;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(subTypes = { VocabularyOfChapterExercisePartialDTO.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VocabularyOfChapterExercisePartialDTO.class, name = "VocabularyOfChapterExercisePartialDTO")
})
@AllArgsConstructor @Getter
public abstract sealed class ExercisePartialDTO permits VocabularyOfChapterExercisePartialDTO{
    private final @NotNull UUID id;

     public static @NotNull ExercisePartialDTO fromExercise(@NotNull Exercise exercise) {
        return switch (exercise) {
            case VocabularyOfChapterExercise vocabularyOfChapterExercise -> VocabularyOfChapterExercisePartialDTO.fromVocabularyOfChapterExercise(vocabularyOfChapterExercise);
            default -> throw new IllegalStateException("Unexpected value: " + exercise);
        };
     }
}
