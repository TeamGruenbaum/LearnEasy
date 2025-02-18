package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

public record ErrorDTO(@NotNull String message) {}
