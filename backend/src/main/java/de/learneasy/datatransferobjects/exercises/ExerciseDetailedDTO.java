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

@Schema(subTypes = { VocabularyOfChapterExerciseDetailedDTO.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VocabularyOfChapterExerciseDetailedDTO.class, name = "VocabularyOfChapterExerciseDetailedDTO")
})
@AllArgsConstructor @Getter
public abstract sealed class ExerciseDetailedDTO permits VocabularyOfChapterExerciseDetailedDTO {
    private final @NotNull UUID id;

     public static @NotNull ExerciseDetailedDTO fromExercise(@NotNull Exercise exercise) {
        return switch (exercise) {
            case VocabularyOfChapterExercise vocabularyOfChapterExercise -> VocabularyOfChapterExerciseDetailedDTO.fromVocabularyOfChapterExercise(vocabularyOfChapterExercise);
            default -> throw new IllegalStateException("Unexpected value: " + exercise);
        };
     }
}
