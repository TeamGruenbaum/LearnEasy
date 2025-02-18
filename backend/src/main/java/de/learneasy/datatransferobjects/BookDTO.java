package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookDTO(
        @NotNull UUID id,
        @NotNull String isbn,
        @NotNull String title,
        @NotNull String publisher
) {}