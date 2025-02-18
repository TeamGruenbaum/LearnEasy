package de.learneasy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;

import java.util.List;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher extends User
{
    @Column(nullable = false) @OneToMany(mappedBy = "teacher")
    @NotNull
    protected List<@NotNull Subject> subjects;

    public static @NotNull Pair<@NotNull String, @NotNull Teacher> create() {
        final @NotNull Teacher teacher = new Teacher();
        return Pair.of(teacher.username, teacher);
    }

    @Override
    public @NotNull List<@NotNull Subject> getSubjects() {
        return subjects;
    }
}