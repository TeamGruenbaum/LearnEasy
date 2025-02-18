package de.learneasy.model;

import de.learneasy.model.exercises.Exercise;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @Getter @EqualsAndHashCode(of = "id")
public class Subject
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private @NotNull UUID id;

    private @NotNull String name;

    @ManyToOne
    private @NotNull Book book;

    private @NotNull RegistrationCode registrationCode = new RegistrationCode();

    @ManyToOne
    private @NotNull Teacher teacher;

    @ManyToMany(mappedBy = "subjects")
    private @NotNull List<@NotNull Pupil> pupils = new ArrayList<>();

    @OneToMany
    private @NotNull List<@NotNull Exercise> exercises = new ArrayList<>();

    public Subject(final @NotNull String name, final @NotNull Book book, final @NotNull Teacher teacher) {
        this.name = name;
        this.book = book;
        this.teacher = teacher;
    }

    public void addExercise(final @NotNull Exercise exercise) {
        if(exercises.contains(exercise)) throw new IllegalArgumentException("Passed exercise already is an exercise of this subject.");
        exercises.add(exercise);
    }

    public void resetRegistrationCode() {
        throw new NotImplementedException();
    }

    public void addPupil(final @NotNull Pupil pupil) {
        if(pupils.contains(pupil)) throw new IllegalArgumentException("Passed pupil already is a pupil of this subject.");
        pupils.add(pupil);
    }

    public int getExerciseStatistics(final @NotNull Exercise exercise) {
        throw new NotImplementedException();
    }
}
