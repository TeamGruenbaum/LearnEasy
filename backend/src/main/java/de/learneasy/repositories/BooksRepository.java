package de.learneasy.repositories;

import de.learneasy.model.Book;
import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Book, UUID> {
    @NotNull Page<@NotNull Book> findAll(@NotNull Pageable pageable);
    @NotNull Optional<@NotNull Book> findByIsbn(@NotNull String isbn);

    @Query("SELECT exercisetemplate FROM Book book JOIN book.exerciseTemplates exercisetemplate WHERE book.id = :bookId AND (:chapter IS NULL OR exercisetemplate.chapter = :chapter)")
    @NotNull Page<@NotNull ExerciseTemplate> findExerciseTemplatesByBookId(@Param("bookId") @NotNull UUID bookId, @Param("chapter") @Nullable String chapter, @NotNull Pageable pageable);

    @Query("SELECT exercisetemplate FROM Book book JOIN book.exerciseTemplates exercisetemplate WHERE book.id = :bookId AND (:chapter IS NULL OR exercisetemplate.chapter = :chapter)")
    @NotNull List<@NotNull ExerciseTemplate> findExerciseTemplatesByBookId(@Param("bookId") @NotNull UUID bookId, @Param("chapter") @Nullable String chapter);
}
