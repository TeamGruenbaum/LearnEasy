package de.learneasy.datatransferobjects.exercises.exercisetemplates;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(subTypes = { VocabularyOfChapterExerciseTemplatePartialDTO.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VocabularyOfChapterExerciseTemplatePartialDTO.class, name = "VocabularyOfChapterExerciseTemplatePartialDTO")
})
@AllArgsConstructor @Getter
public abstract sealed class ExerciseTemplatePartialDTO permits VocabularyOfChapterExerciseTemplatePartialDTO {
    private final @NotNull UUID id;
    private final @NotNull String name;
    private final @NotNull String chapter;

    public static @NotNull ExerciseTemplatePartialDTO fromExerciseTemplate(@NotNull ExerciseTemplate exerciseTemplate) {
        return switch (exerciseTemplate) {
            case VocabularyOfChapterExerciseTemplate vocabularyOfChapterExerciseTemplate -> VocabularyOfChapterExerciseTemplatePartialDTO.fromVocabularyOfChapterExerciseTemplate(vocabularyOfChapterExerciseTemplate);
            default -> throw new IllegalStateException("Unexpected value: " + exerciseTemplate);
        };
    }
}
