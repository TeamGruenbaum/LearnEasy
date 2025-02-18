package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BooksPageDTO(
    @NotNull Integer maximumPage,
    @NotNull List<@NotNull BookDTO> books
) {}
