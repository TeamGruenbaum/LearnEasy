package de.learneasy.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

@Entity @Table(name = "\"user\"") @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "discriminator_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(of = "username")
public abstract class User {
    @Id
    protected @NotNull String username = createUsername();

    protected static @NotNull String createUsername() {
        final @NotNull Random random = new Random();

        final @NotNull List<@NotNull String> adjectives = List.of("Blauer", "Roter", "Gelber", "Grüner", "Schlauer", "Lustiger", "Netter", "Ehrgeiziger", "Fröhlicher", "Fleißiger", "Gutmütiger", "Schneller", "Kreativer", "Neugieriger", "Ruhiger", "Cleverer", "Treuer", "Ehrlicher", "Mutiger", "Flinker");
        final @NotNull List<@NotNull String> animals = List.of("Delfin", "Hund", "Fuchs", "Waschbär", "Elefant", "Hamster", "Koala", "Hirsch", "Igel", "Dachs", "Adler", "Hase", "Wolf", "Löwe", "Affe", "Otter", "Gepard", "Flamingo", "Seelöwe", "Panda");
        final int randomFourDigitNumber = random.nextInt(10000);

        return adjectives.get(random.nextInt(adjectives.size())) + animals.get(random.nextInt(animals.size())) + randomFourDigitNumber;
    }

    public abstract @NotNull List<@NotNull Subject> getSubjects();
}
