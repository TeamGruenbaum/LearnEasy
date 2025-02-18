package de.learneasy.controllers.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class InternalServerErrorException extends RuntimeException {
    private final @NotNull String message;

    public InternalServerErrorException(@NotNull String message) {
        this.message = message;
    }
}
