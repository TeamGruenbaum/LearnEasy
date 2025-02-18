package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubjectDetailedDTO(
    @NotNull UUID id,
    @NotNull String name,
    @NotNull BookDTO book
){}
