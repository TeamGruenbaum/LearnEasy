package de.learneasy.repositories;

import de.learneasy.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {
    @NotNull Optional<@NotNull User> findByUsername(@NotNull String username);
}
