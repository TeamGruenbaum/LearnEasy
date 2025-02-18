package de.learneasy.datatransferobjects;

import de.learneasy.model.Presentation;
import jakarta.validation.constraints.NotNull;

public enum PresentationDTO
{
    TextAskForeignLanguage,
    TextAskNativeLanguage,
    TextRandom,
    Pairs;

    public static @NotNull PresentationDTO fromPresentation(Presentation presentation) {
        return switch (presentation) {
            case TextAskForeignLanguage -> TextAskForeignLanguage;
            case TextAskNativeLanguage -> TextAskNativeLanguage;
            case TextRandom -> TextRandom;
            case Pairs -> Pairs;
        };
    }

    public @NotNull Presentation toPresentation() {
        return switch (this) {
            case TextAskForeignLanguage -> Presentation.TextAskForeignLanguage;
            case TextAskNativeLanguage -> Presentation.TextAskNativeLanguage;
            case TextRandom -> Presentation.TextRandom;
            case Pairs -> Presentation.Pairs;
        };
    }
}
