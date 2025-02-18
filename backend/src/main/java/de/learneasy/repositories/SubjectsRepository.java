package de.learneasy.repositories;

import de.learneasy.model.Subject;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubjectsRepository extends JpaRepository<Subject, UUID> {
    @Query("SELECT subject FROM Subject subject WHERE subject.teacher.username = :username OR :username IN (SELECT pupil.username FROM subject.pupils pupil)")
    @NotNull Page<@NotNull Subject> findAllSubjectsByUsername(@Param("username") @NotNull String username, @NotNull Pageable pageable);
}
