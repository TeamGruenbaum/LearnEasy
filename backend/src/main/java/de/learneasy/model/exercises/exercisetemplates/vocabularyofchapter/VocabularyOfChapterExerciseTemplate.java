package de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter;

import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(callSuper = true)
public class VocabularyOfChapterExerciseTemplate extends ExerciseTemplate
{
    @OneToMany
    private @NotNull List<@NotNull VocabularyWord> vocabulary;

    public VocabularyOfChapterExerciseTemplate(final @NotNull String name, final @NotNull String chapter, final @NotNull List<@NotNull VocabularyWord> vocabulary) {
        super(name, chapter);
        this.vocabulary = vocabulary;
    }
}
