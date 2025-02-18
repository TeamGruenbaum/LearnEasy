package de.learneasy.controllers;

import de.learneasy.controllers.exceptions.BadRequestException;
import de.learneasy.controllers.exceptions.NotFoundException;
import de.learneasy.datatransferobjects.RegistrationCodeDTO;
import de.learneasy.datatransferobjects.UsernameDTO;
import de.learneasy.datatransferobjects.users.UserDTO;
import de.learneasy.model.*;
import de.learneasy.openapi.*;
import de.learneasy.repositories.SchoolsRepository;
import de.learneasy.repositories.SubjectsRepository;
import de.learneasy.repositories.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1")
@Tag(name = "2. Users")
@Validated
@Transactional(rollbackFor = { Throwable.class })
public class UsersController {
    private final @NotNull SchoolsRepository schoolsRepository;
    private final @NotNull SubjectsRepository subjectsRepository;
    private final @NotNull UsersRepository usersRepository;

    @Autowired
    protected UsersController(
            final @NotNull SchoolsRepository schoolsRepository,
            final @NotNull SubjectsRepository subjectsRepository,
            final @NotNull UsersRepository usersRepository
    ) {
        this.schoolsRepository = schoolsRepository;
        this.subjectsRepository = subjectsRepository;
        this.usersRepository = usersRepository;
    }


    @Operation(description = "Create a new user.")
    @CommonApiResponses @CreatedApiResponse
    @PostMapping("/users")
    public @NotNull ResponseEntity<UsernameDTO> createUser(
            final @RequestBody RegistrationCodeDTO registrationCodeDTO
    ){
        @NotNull School exampleSchool = schoolsRepository.getExampleSchool();
        @NotNull String username;

        if(exampleSchool.getRegistrationCode().getCode().equals(registrationCodeDTO.registrationCode()))
        {
            @NotNull Pair<@NotNull String, @NotNull Teacher> usernameAndTeacher = Teacher.create();
            username = usernameAndTeacher.getFirst();
            @NotNull Teacher teacher = usernameAndTeacher.getSecond();
            usersRepository.saveAndFlush(teacher);

            exampleSchool.addTeacher(teacher);
            schoolsRepository.save(exampleSchool);
        }
        else
        {
            @NotNull Subject foundSubject = subjectsRepository
                    .findAll()
                    .stream()
                    .filter(subject -> subject.getRegistrationCode().getCode().equals(registrationCodeDTO.registrationCode()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException(List.of("The passed registration code does not belong to any school or subject.")));

            @NotNull Pair<@NotNull String, @NotNull Pupil> usernameAndPupil = Pupil.create();
            username = usernameAndPupil.getFirst();
            @NotNull Pupil pupil = usernameAndPupil.getSecond();
            usersRepository.saveAndFlush(pupil);

            foundSubject.addPupil(pupil);
            exampleSchool.addPupil(pupil);

            subjectsRepository.saveAndFlush(foundSubject);
            schoolsRepository.save(exampleSchool);

            pupil.addSubject(foundSubject);
            usersRepository.saveAndFlush(pupil);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsernameDTO(username));
    }

    @Operation(description = "Get details of a user.")
    @CommonApiResponses @OkApiResponse @NotFoundApiResponse
    @GetMapping("/users/{username}")
    public @NotNull ResponseEntity<UserDTO> getUser(final @PathVariable @NotNull String username){
        @NotNull User user = usersRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with provided username not found."));
        return ResponseEntity.ok(UserDTO.fromUser(user));
    }

    @Operation(description = "Delete a user.")
    @CommonApiResponses @NoContentApiResponse @NotFoundApiResponse
    @DeleteMapping("/users/{username}")
    public @NotNull ResponseEntity<Void> deleteUser(
        final @PathVariable @NotNull String username
    ){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
