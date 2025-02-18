package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ExerciseStatisticsDTO(@Min(0) @NotNull Integer successfulCompletions){}
