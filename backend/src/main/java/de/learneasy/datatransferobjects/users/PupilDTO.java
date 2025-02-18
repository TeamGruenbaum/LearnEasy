package de.learneasy.datatransferobjects.users;

import de.learneasy.model.exercises.Exercise;
import de.learneasy.model.Pupil;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class PupilDTO extends UserDTO {
    private final @NotNull Map<@NotNull UUID, @NotNull Boolean> exerciseProgresses;

    public PupilDTO(@NotNull Map<@NotNull UUID, @NotNull Boolean> exerciseProgresses) {
        super();
        this.exerciseProgresses = exerciseProgresses;
    }

    public static @NotNull PupilDTO fromPupil(Pupil pupil) {
        Map<UUID, Boolean> exerciseProgresses = new HashMap<>();
        for (Map.Entry<@NotNull Exercise, @NotNull Boolean> entry : pupil.getExerciseProgresses().entrySet()) {
            exerciseProgresses.put(entry.getKey().getId(), entry.getValue());
        }
        return new PupilDTO(exerciseProgresses);
    }
}
