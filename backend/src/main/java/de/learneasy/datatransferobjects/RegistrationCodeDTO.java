package de.learneasy.datatransferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationCodeDTO(@NotNull @Size(min = 6, max = 6) String registrationCode) {}
