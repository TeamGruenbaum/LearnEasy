package de.learneasy.datatransferobjects;

import de.learneasy.datatransferobjects.exercises.ExercisePartialDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExercisesPageDTO(
    @Min(0) @NotNull Integer maximumPage,
    @Size(min = 10, max = 100) List<@NotNull ExercisePartialDTO> exercises
){}
