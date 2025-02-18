package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubjectsPageDTO(
        @NotNull Integer maximumPage,
        @NotNull List<@NotNull SubjectPartialDTO> subjects
) {}