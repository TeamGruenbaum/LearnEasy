package de.learneasy.model;

import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyWord;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(of = "id")
public class Book
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private @NotNull UUID id;

    @Getter
    private @NotNull String isbn;

    @Getter
    private @NotNull String title;

    @Getter
    private @NotNull String publisher;

    @OneToMany
    private @NotNull List<@NotNull ExerciseTemplate> exerciseTemplates;

    @Getter
    @OneToMany
    private @NotNull List<@NotNull VocabularyWord> vocabulary;

    public Book(
            final @NotNull String isbn,
            final @NotNull String title,
            final @NotNull String publisher,
            final @NotNull List<@NotNull ExerciseTemplate> exerciseTemplates,
            final @NotNull List<@NotNull VocabularyWord> vocabulary
    ) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.exerciseTemplates = exerciseTemplates;
        this.vocabulary = vocabulary;
    }

    public @NotNull List<@NotNull ExerciseTemplate> getExerciseTemplates(final @Nullable String chapter) {
        if (chapter != null) {
            return exerciseTemplates
                    .stream()
                    .filter(exerciseTemplate -> exerciseTemplate.getChapter().equals(chapter))
                    .toList();
        }

        return exerciseTemplates;
    }
}
