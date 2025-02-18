package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

public record UsernameDTO(@NotNull String username) {}
