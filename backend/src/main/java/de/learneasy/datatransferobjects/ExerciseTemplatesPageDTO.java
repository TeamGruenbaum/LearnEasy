package de.learneasy.datatransferobjects;

import de.learneasy.datatransferobjects.exercises.exercisetemplates.ExerciseTemplatePartialDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExerciseTemplatesPageDTO(
    @Min(0) @NotNull Integer maximumPage,
    @Size(min = 10, max = 100) @NotNull List<@NotNull ExerciseTemplatePartialDTO> exerciseTemplates
){}
