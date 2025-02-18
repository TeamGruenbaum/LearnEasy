package de.learneasy.datatransferobjects.users;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.learneasy.model.Pupil;
import de.learneasy.model.Teacher;
import de.learneasy.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(subTypes = { TeacherDTO.class, PupilDTO.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TeacherDTO.class, name = "TeacherDTO"),
        @JsonSubTypes.Type(value = PupilDTO.class, name = "PupilDTO")
})
@AllArgsConstructor @Getter
public abstract sealed class UserDTO permits TeacherDTO, PupilDTO {
    public static @NotNull UserDTO fromUser(@NotNull User user) {
        return switch (user) {
            case Teacher ignored -> new TeacherDTO();
            case Pupil pupil -> PupilDTO.fromPupil(pupil);
            default -> throw new IllegalStateException("Unexpected value: " + user);
        };
    }
}