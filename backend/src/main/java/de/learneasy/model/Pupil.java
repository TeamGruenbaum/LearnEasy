package de.learneasy.model;

import de.learneasy.model.exercises.Exercise;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pupil extends User
{
    @ElementCollection
    private @NotNull Map<@NotNull Exercise, @NotNull Boolean> exerciseProgresses;

    @ManyToMany
    private @NotNull List<@NotNull Subject> subjects = new ArrayList<>();

    public static @NotNull Pair<@NotNull String, @NotNull Pupil> create() {
        final @NotNull Pupil pupil = new Pupil();
        return Pair.of(pupil.username, pupil);
    }

    @Override
    public @NotNull List<@NotNull Subject> getSubjects() {
        return subjects;
    }

    public void addSubject(final @NotNull Subject subject) {
        if(subjects.contains(subject)) throw new IllegalArgumentException("Passed subject already is a subject of this pupil.");
        subjects.add(subject);
    }

    public void leaveSubject(final @NotNull Subject subject) {
        throw new NotImplementedException();
    }
}