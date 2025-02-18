package de.learneasy.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.Random;

@Embeddable
@Getter @EqualsAndHashCode
public class RegistrationCode
{
    private @NotNull String code;

    private @NotNull ZonedDateTime expirationDate;

    public RegistrationCode() {
        final @NotNull StringBuilder code = new StringBuilder();
        for(int i = 0; i < 6; i++) code.append((char) (new Random().nextInt(65, 91)));

        this.code = code.toString();
        this.expirationDate = ZonedDateTime.now().plusHours(3);
    }
}