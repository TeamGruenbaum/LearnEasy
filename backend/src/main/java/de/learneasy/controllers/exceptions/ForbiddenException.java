package de.learneasy.controllers.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ForbiddenException extends RuntimeException {
    private final @NotNull String message;

    public ForbiddenException(@NotNull String message) {
        this.message = message;
    }
}