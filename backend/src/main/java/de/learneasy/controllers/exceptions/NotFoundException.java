package de.learneasy.controllers.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class NotFoundException extends RuntimeException {
    private final @NotNull String message;

    public NotFoundException(@NotNull String message) {
        this.message = message;
    }
}