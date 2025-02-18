package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubjectPartialDTO(
        @NotNull UUID id,
        @NotNull String name
) {}