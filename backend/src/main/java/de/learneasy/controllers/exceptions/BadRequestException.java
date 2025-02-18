package de.learneasy.controllers.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private final @NotNull List<@NotNull String> messages;

    public BadRequestException(@NotNull List<String> messages) {
        super(String.join(", ", messages));
        this.messages = messages;
    }
}