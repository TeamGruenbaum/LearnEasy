package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubjectCreationDTO(
    @NotNull String name,
    @NotNull UUID bookId
) {}
