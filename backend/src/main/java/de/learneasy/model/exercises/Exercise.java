package de.learneasy.model.exercises;

import jakarta.persistence.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "discriminator_type", columnDefinition = "VARCHAR(255)")
@Getter
public abstract class Exercise {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private @NotNull UUID id;

    protected Exercise() {}
}