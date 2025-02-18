package de.learneasy.controllers;

import de.learneasy.controllers.exceptions.BadRequestException;
import de.learneasy.controllers.exceptions.ForbiddenException;
import de.learneasy.controllers.exceptions.NotFoundException;
import de.learneasy.datatransferobjects.*;
import de.learneasy.model.*;
import de.learneasy.openapi.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1")
@Tag(name = "3. Subjects")
@Validated
@Transactional(rollbackFor = { Throwable.class })
public class SubjectsController {
    private final @NotNull SubjectsRepository subjectsRepository;
    private final @NotNull BooksRepository booksRepository;
    private final @NotNull UsersRepository usersRepository;

    @Autowired
    protected SubjectsController(
            final @NotNull SubjectsRepository subjectsRepository,
            final @NotNull BooksRepository booksRepository,
            final @NotNull UsersRepository usersRepository
    ) {
        this.subjectsRepository = subjectsRepository;
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
    }

    @Operation(description = "Create a new subject for a user.")
    @CommonApiResponses @CreatedApiResponse @NotFoundApiResponse @ForbiddenApiResponse
    @PostMapping("/users/{username}/subjects")
    public @NotNull ResponseEntity<RegistrationCodeDTO> createSubjectInUser(
            final @PathVariable @NotNull String username,
            final @RequestBody @NotNull SubjectCreationDTO subjectCreationDTO
    ) {
        @NotNull User user = usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username "+username+" not found."));
        if(!(user instanceof Teacher)) throw new ForbiddenException("User with username "+username+" is not a teacher.");

        @NotNull Book book = booksRepository.findById(subjectCreationDTO.bookId()).orElseThrow(() -> new NotFoundException("Book with id "+subjectCreationDTO.bookId()+" not found."));
        @NotNull Subject subject = new Subject(subjectCreationDTO.name(), book, (Teacher) user);
        subjectsRepository.save(subject);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrationCodeDTO(subject.getRegistrationCode().getCode()));
    }

    @Operation(description = "Get the registration code of a subject of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}/subjects/{subjectId}/registration-code")
    public @NotNull ResponseEntity<RegistrationCodeDTO> getRegistrationCodeOfSubjectOfUser(
            final @PathVariable @NotNull String username,
            final @PathVariable @NotNull UUID subjectId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Operation(description = "Join a subject as a user.")
    @CommonApiResponses @NoContentApiResponse @NotFoundApiResponse @ForbiddenApiResponse
    @PostMapping("/users/{username}/subjects/join")
    public @NotNull ResponseEntity<Void> joinSubjectOfUser(
            final @PathVariable @NotNull String username,
            final @RequestBody @NotNull RegistrationCodeDTO registrationCodeDTO
    ){
        @NotNull User user = usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username "+username+" not found."));
        if(!(user instanceof Pupil)) {
            throw new ForbiddenException("User with username "+username+" is not a pupil.");
        }

        @NotNull Subject foundSubject = subjectsRepository
                .findAll()
                .stream().filter(subject -> subject.getRegistrationCode().getCode().equals(registrationCodeDTO.registrationCode()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(List.of("The passed registration code does not belong to any subject.")));

        foundSubject.addPupil((Pupil) user);

        subjectsRepository.saveAndFlush(foundSubject);

        ((Pupil) user).addSubject(foundSubject);
        usersRepository.saveAndFlush(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(description = "Get all subjects of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("users/{username}/subjects")
    public @NotNull ResponseEntity<SubjectsPageDTO> getSubjectsOfUser(
            final @PathVariable @NotNull String username,
            final @RequestParam(name = "perPage", defaultValue = "5", required = false) @Min(5) @Max(20) @Nullable Integer perPage,
            final @RequestParam(name = "pageNumber", required = false) @Min(1) @Nullable Integer pageNumber
    ) {
        @NotNull User user = usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username "+username+" not found."));

        if(pageNumber == null) {
            return ResponseEntity.ok(
                    new SubjectsPageDTO(
                            1,
                            user
                                    .getSubjects()
                                    .stream()
                                    .map(subject -> new SubjectPartialDTO(subject.getId(), subject.getName()))
                                    .toList()
                    )
            );
        }

        @NotNull Page<@NotNull Subject> page = subjectsRepository.findAllSubjectsByUsername(username, PageRequest.of(pageNumber-1, perPage));
        return ResponseEntity.ok(
                new SubjectsPageDTO(
                        page.getTotalPages(),
                        page.getContent().stream().map(subject ->
                                new SubjectPartialDTO(subject.getId(), subject.getName())
                        ).toList()
                )
        );
    }

    @Operation(description = "Get details of a subject of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}/subjects/{subjectId}")
    public @NotNull ResponseEntity<SubjectDetailedDTO> getSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId
    ) {
        final @NotNull Subject searchedSubject = usersRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with provided username not found."))
                .getSubjects()
                .stream()
                .filter(subject -> subjectId.equals(subject.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Subject with id " + subjectId + " not found."));

        return ResponseEntity.ok(
                new SubjectDetailedDTO(
                        searchedSubject.getId(),
                        searchedSubject.getName(),
                        new BookDTO(
                                searchedSubject.getBook().getId(),
                                searchedSubject.getBook().getIsbn(),
                                searchedSubject.getBook().getTitle(),
                                searchedSubject.getBook().getPublisher()
                        )
                )
        );
    }

    @Operation(description = "Delete a subject of a user.")
    @CommonApiResponses @NoContentApiResponse @NotFoundApiResponse
    @DeleteMapping("/users/{username}/subjects/{subjectId}")
    public @NotNull ResponseEntity<Void> deleteSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
