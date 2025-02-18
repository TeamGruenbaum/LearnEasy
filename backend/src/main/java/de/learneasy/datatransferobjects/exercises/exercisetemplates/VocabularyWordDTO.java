package de.learneasy.datatransferobjects.exercises.exercisetemplates;

import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyWord;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Base64;
import java.util.UUID;

public record VocabularyWordDTO(
    @NotNull UUID id,
    @NotNull String foreignLanguage,
    @NotNull String nativeLanguage,
    @NotNull String chapter,
    @NotNull Integer number,
    @Nullable String image,
    @Nullable String audio
){
    public static @NotNull VocabularyWordDTO fromVocabularyWord(VocabularyWord vocabularyWord) {
        final Base64.@NotNull Encoder base64encoder = Base64.getEncoder();

        return new VocabularyWordDTO(
            vocabularyWord.getId(),
            vocabularyWord.getForeignLanguage(),
            vocabularyWord.getNativeLanguage(),
            vocabularyWord.getChapter(),
            vocabularyWord.getNumber(),
            vocabularyWord.getImage() != null ? base64encoder.encodeToString(vocabularyWord.getImage()) : null,
            vocabularyWord.getAudio() != null ? base64encoder.encodeToString(vocabularyWord.getAudio()) : null
        );
    }
}
