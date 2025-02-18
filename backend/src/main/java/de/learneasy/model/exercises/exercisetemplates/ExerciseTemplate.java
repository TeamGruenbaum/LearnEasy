package de.learneasy.model.exercises.exercisetemplates;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "discriminator_type", columnDefinition = "VARCHAR(255)")
@Getter @NoArgsConstructor @EqualsAndHashCode(of = "id")
public abstract class ExerciseTemplate {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private @NotNull UUID id;

    private @NotNull String name;

    private @NotNull String chapter;

    protected ExerciseTemplate(final @NotNull String name, final @NotNull String chapter) {
        this.name = name;
        this.chapter = chapter;
    }
}
