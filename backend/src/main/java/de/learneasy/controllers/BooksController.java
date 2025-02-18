package de.learneasy.controllers;

import de.learneasy.controllers.exceptions.NotFoundException;
import de.learneasy.datatransferobjects.BooksPageDTO;
import de.learneasy.datatransferobjects.BookDTO;
import de.learneasy.datatransferobjects.ChaptersPageDTO;
import de.learneasy.datatransferobjects.ExerciseTemplatesPageDTO;
import de.learneasy.datatransferobjects.exercises.exercisetemplates.ExerciseTemplatePartialDTO;
import de.learneasy.model.Book;
import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import de.learneasy.openapi.CommonApiResponses;
import de.learneasy.openapi.NotFoundApiResponse;
import de.learneasy.openapi.OkApiResponse;
import de.learneasy.repositories.BooksRepository;
import de.learneasy.repositories.SubjectsRepository;
import de.learneasy.repositories.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1")
@Tag(name = "1. Books")
@Validated
@Transactional(rollbackFor = { Throwable.class })
public class BooksController
{
    private final @NotNull BooksRepository booksRepository;

    @Autowired
    protected BooksController(
        final @NotNull BooksRepository booksRepository
    ) {
        this.booksRepository = booksRepository;
    }

    @Operation(description = "Get all books.")
    @CommonApiResponses @OkApiResponse
    @GetMapping("/books")
    public @NotNull ResponseEntity<BooksPageDTO> getBooks(
            final @RequestParam(name = "perPage", defaultValue = "5", required = false) @Min(5) @Max(20) @Nullable Integer perPage,
            final @RequestParam(name = "pageNumber", required = false) @Min(1) @Nullable Integer pageNumber
    ) {
        if(pageNumber == null) {
            return ResponseEntity.ok(
                new BooksPageDTO(
                1,
                booksRepository
                    .findAll()
                    .stream()
                    .map(book -> new BookDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getPublisher()))
                    .toList()
                )
            );
        }

        @NotNull Page<@NotNull Book> page = booksRepository.findAll(PageRequest.of(pageNumber-1, perPage));
        return ResponseEntity.ok(
            new BooksPageDTO(
                page.getTotalPages(),
                page.getContent().stream().map(book ->
                        new BookDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getPublisher())
                ).toList()
            )
        );
    }

    @Operation(description = "Get all chapters of a book.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/books/{bookId}/chapters")
    public @NotNull ResponseEntity<ChaptersPageDTO> getChaptersOfBook(
            final @PathVariable @NotNull UUID bookId,
            final @RequestParam(name = "perPage", defaultValue = "50", required = false) @Min(10) @Max(100) @Nullable Integer perPage,
            final @RequestParam(name = "pageNumber", required = false) @Min(1) @Nullable Integer pageNumber
    ) {
        final @NotNull List<@NotNull String> chapters = booksRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with provided id " + bookId + " not found"))
                .getExerciseTemplates(null)
                .stream()
                .map(ExerciseTemplate::getChapter)
                .toList();

        if(pageNumber == null) {
            return ResponseEntity.ok(
                new ChaptersPageDTO(
                    1,
                    chapters
                )
            );
        }

        return ResponseEntity.ok(
            new ChaptersPageDTO(
                (int) Math.ceil((double) chapters.size() / perPage),
                    chapters
                            .stream()
                            .skip((long) (pageNumber-1) * perPage)
                            .limit(perPage)
                            .toList()
            )
        );
    }

    @Operation(description = "Get all exercise templates of a book.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/books/{bookId}/exercise-templates")
    public @NotNull ResponseEntity<ExerciseTemplatesPageDTO> getExerciseTemplatesOfBook(
         final @PathVariable @NotNull UUID bookId,
         final @RequestParam(name = "chapter", required = false) @Nullable String chapter,
         final @RequestParam(name = "perPage", defaultValue = "50", required = false) @Min(10) @Max(100) @Nullable Integer perPage,
         final @RequestParam(name = "pageNumber", required = false) @Min(1) @Nullable Integer pageNumber
    ) {
        booksRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with provided id " + bookId + " not found"));

        if(pageNumber == null) {
            return ResponseEntity.ok(
                new ExerciseTemplatesPageDTO(
                    1,
                    booksRepository
                        .findExerciseTemplatesByBookId(bookId, chapter)
                        .stream()
                        .map(ExerciseTemplatePartialDTO::fromExerciseTemplate)
                        .toList()
                )
            );
        }

        final @NotNull Page<@NotNull ExerciseTemplate> exerciseTemplates = booksRepository.findExerciseTemplatesByBookId(bookId, chapter, PageRequest.of(pageNumber-1, perPage));
        return ResponseEntity.ok(
            new ExerciseTemplatesPageDTO(
                exerciseTemplates.getTotalPages(),
                exerciseTemplates.getContent().stream().map(ExerciseTemplatePartialDTO::fromExerciseTemplate).toList()
            )
        );
    }
}