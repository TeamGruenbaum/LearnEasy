package de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(of = "id")
public class VocabularyWord {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private @NotNull UUID id;

    private @NotNull String foreignLanguage;

    private @NotNull String nativeLanguage;

    private @NotNull String chapter;

    private int number;

    @Column(columnDefinition = "BYTEA") @Basic(fetch = FetchType.LAZY)
    private byte@Nullable [] image;

    @Column(columnDefinition = "BYTEA") @Basic(fetch = FetchType.LAZY)
    private byte@Nullable[] audio;

    public VocabularyWord(
            final @NotNull String foreignLanguage,
            final @NotNull String nativeLanguage,
            final @NotNull String chapter,
            final int number,
            final byte@Nullable[] image,
            final byte@Nullable[] audio
    ) {
        this.foreignLanguage = foreignLanguage;
        this.nativeLanguage = nativeLanguage;
        this.chapter = chapter;
        this.number = number;
        this.image = image;
        this.audio = audio;
    }
}