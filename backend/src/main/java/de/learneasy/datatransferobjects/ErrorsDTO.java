package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ErrorsDTO(@NotNull List<@NotNull String> messages) {}
