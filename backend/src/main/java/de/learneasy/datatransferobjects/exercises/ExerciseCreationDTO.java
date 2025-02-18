package de.learneasy.datatransferobjects.exercises;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(subTypes = { VocabularyOfChapterExerciseCreationDTO.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = VocabularyOfChapterExerciseCreationDTO.class, name = "VocabularyOfChapterExerciseCreationDTO")
})
@AllArgsConstructor @Getter
public abstract sealed class ExerciseCreationDTO permits VocabularyOfChapterExerciseCreationDTO {
    private final @NotNull UUID exerciseTemplateId;
}
