package de.learneasy.controllers;

import de.learneasy.controllers.exceptions.ForbiddenException;
import de.learneasy.controllers.exceptions.NotFoundException;
import de.learneasy.datatransferobjects.*;
import de.learneasy.datatransferobjects.exercises.ExerciseCreationDTO;
import de.learneasy.datatransferobjects.exercises.ExerciseDetailedDTO;
import de.learneasy.datatransferobjects.exercises.ExercisePartialDTO;
import de.learneasy.datatransferobjects.exercises.VocabularyOfChapterExerciseCreationDTO;
import de.learneasy.model.*;
import de.learneasy.model.exercises.Exercise;
import de.learneasy.model.exercises.vocabularyofchapter.VocabularyOfChapterExercise;
import de.learneasy.model.exercises.exercisetemplates.ExerciseTemplate;
import de.learneasy.model.exercises.exercisetemplates.vocabularyofchapter.VocabularyOfChapterExerciseTemplate;
import de.learneasy.openapi.*;
import de.learneasy.repositories.ExercisesRepository;
import de.learneasy.repositories.SubjectsRepository;
import de.learneasy.repositories.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("v1")
@Tag(name = "4. Exercises")
@Validated
@Transactional(rollbackFor = { Throwable.class })
public class ExercisesController {
    private final @NotNull UsersRepository usersRepository;
    private final @NotNull SubjectsRepository subjectsRepository;
    private final @NotNull ExercisesRepository exercisesRepository;

    @Autowired
    protected ExercisesController(
        final @NotNull UsersRepository usersRepository,
        final @NotNull SubjectsRepository subjectsRepository,
        final @NotNull ExercisesRepository exercisesRepository
    ) {
        this.usersRepository = usersRepository;
        this.subjectsRepository = subjectsRepository;
        this.exercisesRepository = exercisesRepository;
    }

    @Operation(description = "Create a new exercise for a subject of a user.")
    @CommonApiResponses @CreatedApiResponse @NotFoundApiResponse @ForbiddenApiResponse
    @PostMapping("/users/{username}/subjects/{subjectId}/exercises")
    public @NotNull ResponseEntity<IdDTO> createExerciseInSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId,
        final @RequestBody @NotNull ExerciseCreationDTO exerciseCreationDTO
    ) {
        final @NotNull User actingUser = usersRepository
                                            .findByUsername(username)
                                            .orElseThrow(() -> new NotFoundException("User with provided username not found"));
        if(!(actingUser instanceof Teacher)) throw new ForbiddenException("Only teachers can create exercises");
        final @NotNull Subject subjectToAddExercise = actingUser
                                                        .getSubjects()
                                                        .stream()
                                                        .filter(subject -> subject.getId().equals(subjectId))
                                                        .findFirst()
                                                        .orElseThrow(() -> new NotFoundException("Subject with provided id " + subjectId + " not found"));
        final @NotNull ExerciseTemplate exerciseTemplateToUse = subjectToAddExercise
                                                                    .getBook()
                                                                    .getExerciseTemplates(null)
                                                                    .stream()
                                                                    .filter(exerciseTemplate -> exerciseTemplate.getId().equals(exerciseCreationDTO.getExerciseTemplateId()))
                                                                    .findFirst()
                                                                    .orElseThrow(() -> new NotFoundException("Exercise template with provided id " + exerciseCreationDTO.getExerciseTemplateId() + " not found"));

        final @NotNull Exercise newExercise = switch (exerciseCreationDTO) {
            case VocabularyOfChapterExerciseCreationDTO vocabularyOfChapterExerciseCreationDTO -> {
                if(!(exerciseTemplateToUse instanceof VocabularyOfChapterExerciseTemplate)) throw new ForbiddenException("Exercise template with provided id " + exerciseCreationDTO.getExerciseTemplateId() + " is not a vocabulary of chapter exercise template");

                yield new VocabularyOfChapterExercise(
                        (VocabularyOfChapterExerciseTemplate) exerciseTemplateToUse,
                        vocabularyOfChapterExerciseCreationDTO.getPresentation() == null ? null : vocabularyOfChapterExerciseCreationDTO.getPresentation().toPresentation(),
                        vocabularyOfChapterExerciseCreationDTO.getSelectedVocabularyWordNumbers(),
                        vocabularyOfChapterExerciseCreationDTO.getNumberOfCorrectAnswersForCompletion()
                );
            }
        };
        exercisesRepository.saveAndFlush(newExercise);
        subjectToAddExercise.addExercise(newExercise);
        subjectsRepository.saveAndFlush(subjectToAddExercise);

        return ResponseEntity.status(HttpStatus.CREATED).body(new IdDTO(newExercise.getId()));
    }

    @Operation(description = "Get all exercises of a subject of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}/subjects/{subjectId}/exercises")
    public @NotNull ResponseEntity<ExercisesPageDTO> getExercisesOfSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId,
        final @RequestParam(name = "perPage", defaultValue = "50", required = false) @Min(10) @Max(100) @Nullable Integer perPage,
        final @RequestParam(name = "pageNumber", required = false) @Min(1) @Nullable Integer pageNumber
    ) {
        final @NotNull List<@NotNull Exercise> searchedExercises = usersRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with provided username not found."))
                .getSubjects()
                .stream()
                .filter(subject -> subjectId.equals(subject.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Subject with id " + subjectId + " not found."))
                .getExercises();


        if (pageNumber == null) {
            return ResponseEntity.ok(
                    new ExercisesPageDTO(
                            1,
                            searchedExercises.stream().map(ExercisePartialDTO::fromExercise).toList()
                    )
            );
        }

        return ResponseEntity.ok(
                new ExercisesPageDTO(
                        (int) Math.ceil((double) searchedExercises.size() / perPage),
                        searchedExercises
                                .stream()
                                .map(ExercisePartialDTO::fromExercise)
                                .skip((long) (pageNumber-1) * perPage)
                                .limit(perPage)
                                .toList()
                )
        );
    }

    @Operation(description = "Get details of an exercise of a subject of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}/subjects/{subjectId}/exercises/{exerciseId}")
    public @NotNull ResponseEntity<ExerciseDetailedDTO> getExerciseOfSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId,
        final @PathVariable @NotNull UUID exerciseId
    ) {
        final @NotNull Exercise searchedExercise = usersRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with provided username not found"))
                .getSubjects()
                .stream()
                .filter(subject -> subjectId.equals(subject.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Subject with id " + subjectId + " not found"))
                .getExercises()
                .stream()
                .filter(exercise -> exerciseId.equals(exercise.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Exercise with id " + exerciseId + " not found"));

        return ResponseEntity.ok(ExerciseDetailedDTO.fromExercise(searchedExercise));
    }

    @Operation(description = "Mark an exercise of a subject of a user as successful.")
    @CommonApiResponses @NoContentApiResponse @NotFoundApiResponse @ForbiddenApiResponse
    @PostMapping("/users/{username}/subjects/{subjectId}/exercises/{exerciseId}/success")
    public @NotNull ResponseEntity<Void> markExerciseOfSubjectOfUserAsSuccessful(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId,
        final @PathVariable @NotNull UUID exerciseId
    ) {
        final @NotNull User actingUser = usersRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with provided username not found"));
        if(!(actingUser instanceof Pupil)) throw new ForbiddenException("User with provided username is not a pupil");
        final @NotNull Exercise exerciseToMarkAsSuccessful = actingUser
                .getSubjects()
                .stream()
                .filter(subject -> subjectId.equals(subject.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Subject with id " + subjectId + " not found"))
                .getExercises()
                .stream()
                .filter(exercise -> exerciseId.equals(exercise.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Exercise with id " + exerciseId + " not found"));

        ((Pupil) actingUser).getExerciseProgresses().put(exerciseToMarkAsSuccessful, true);
        usersRepository.save(actingUser);

        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Get statistics of an exercise of a subject of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}/subjects/{subjectId}/exercises/{exerciseId}/statistics")
    public @NotNull ResponseEntity<ExerciseStatisticsDTO> getStatisticsOfExerciseOfSubjectOfUser(
        final @PathVariable @NotNull String username,
        final @PathVariable @NotNull UUID subjectId,
        final @PathVariable @NotNull UUID exerciseId
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}

