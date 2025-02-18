package de.learneasy.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

@Entity
@EqualsAndHashCode(of = "id")
public class School
{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private @NotNull UUID id;

    @Getter
    private @NotNull String name;

    @Getter
    private @NotNull String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Getter
    private @NotNull Map<@NotNull Integer, @NotNull Character> adminTable;

    private @Nullable String requiredAdminTablePositions = null;

    @Getter
    private @NotNull RegistrationCode registrationCode = new RegistrationCode();

    @OneToMany
    @Getter
    private @NotNull List<@NotNull Teacher> teachers = new ArrayList<>();

    @OneToMany
    @Getter
    private @NotNull List<@NotNull Pupil> pupils = new ArrayList<>();

    protected School() {
        final @NotNull Random random = new Random();
        final @NotNull Map<@NotNull Integer, @NotNull Character> adminTable = new HashMap<>();
        final @NotNull String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*#&!?$%*";
        for(int counter: IntStream.rangeClosed(1, 20).toArray()) adminTable.put(counter, characters.charAt(random.nextInt(characters.length())));
        this.adminTable = adminTable;
    }

    protected School(final @NotNull String name, final @NotNull String email) {
        this();
        this.name = name;
        this.email = email;
    }

    public static @NotNull Triple<@NotNull RegistrationCode, @NotNull Map<@NotNull Integer, @NotNull Character>, @NotNull School> create(final @NotNull String name, final @NotNull String email) {
        final @NotNull School school = new School(name, email);
        return Triple.of(school.registrationCode, school.adminTable, school);
    }

    public @NotNull String generateAdminTablePositions() {
        throw new NotImplementedException();
    }

    public @NotNull RegistrationCode resetRegistrationCode(final @NotNull String adminTablePositionsValues) {
        throw new NotImplementedException();
    }

    public void addTeacher(final @NotNull Teacher teacher) {
        if(teachers.contains(teacher)) throw new IllegalArgumentException("Passed teacher already is a teacher of this school.");
        teachers.add(teacher);
    }

    public void addPupil(final @NotNull Pupil pupil) {
        if(pupils.contains(pupil)) throw new IllegalArgumentException("Passed pupil already is a pupil of this school.");
        pupils.add(pupil);
    }
}