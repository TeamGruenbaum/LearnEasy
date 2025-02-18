package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChaptersPageDTO (
    @NotNull Integer maximumPage,
    @NotNull List<@NotNull String> chapters
){}
