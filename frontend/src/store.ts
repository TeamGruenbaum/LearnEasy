import {ref, type Ref} from "vue"
import type {StoreDefinition} from "pinia"
import {defineStore} from "pinia"
import {ServerAccessor} from "@/data/server/ServerAccessor.ts";
import type {SubjectPartialDTO} from "@/data/server/dataTransferObjects/SubjectPartialDTO.ts";
import type {SubjectDetailedDTO} from "@/data/server/dataTransferObjects/SubjectDetailedDTO.ts";
import type {ExercisePartialDTO} from "@/data/server/dataTransferObjects/exercises/ExercisePartialDTO.ts";
import type {BookDTO} from "@/data/server/dataTransferObjects/BookDTO.ts";
import type {RegistrationCodeDTO} from "@/data/server/dataTransferObjects/RegistrationCodeDTO.ts";
import {LocalAccessor} from "@/data/LocalAccessor.ts";
import type {UserDTO} from "@/data/server/dataTransferObjects/users/UserDTO.ts";
import type {ExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/ExerciseDetailedDTO.ts";
import type {ExerciseTemplatePartialDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/ExerciseTemplatePartialDTO.ts";
import type {ExerciseCreationDTO} from "@/data/server/dataTransferObjects/exercises/ExerciseCreationDTO.ts";

export const useStore: StoreDefinition<
    "store",
    {
        currentUsername: Ref<string | undefined>,
        currentUser: Ref<UserDTO | undefined>,
        allBooks: Ref<ReadonlyArray<BookDTO> | undefined>,
        newSubjectName: Ref<string>,
        newSubjectBookId: Ref<string | undefined>,
        newSubjectRegistrationCode: Ref<string | undefined>,
        subjects: Ref<ReadonlyArray<SubjectPartialDTO>|undefined>,
        currentSubject: Ref<SubjectDetailedDTO | undefined>,
        chaptersOfBookOfCurrentSubject: Ref<ReadonlyArray<string> | undefined>,
        exerciseTemplatesOfBookOfCurrentSubject: Ref<ReadonlyArray<ExerciseTemplatePartialDTO> | undefined>,
        exercisesOfBookOfCurrentSubject: Ref<ReadonlyArray<ExercisePartialDTO> | undefined>,
        currentExercise: Ref<ExerciseDetailedDTO | undefined>,
        newExerciseChapter: Ref<string|undefined>,
        newExerciseTemplateId: Ref<string|undefined>,
    },
    {},
    {
        registerUser: (registrationCode: string) => Promise<void>,
        loginUser: (username: string) => Promise<void>,
        refreshUser:() => Promise<void>,
        finishExerciseExecution: () => Promise<void>,
        getBooks: () => Promise<void>,
        createSubject: () => Promise<void>,
        getSubjects: () => Promise<void>,
        joinSubject: (registrationCode: string) => Promise<void>,
        getSubject: (subjectId: string) => Promise<void>,
        getExercise: (exerciseId: string) => Promise<void>,
        createExercise: (exerciseCreation: ExerciseCreationDTO) => Promise<void>
        reset: () => void
    }
> = defineStore("store", () => {
    const currentUsername: Ref<string | undefined> = ref(LocalAccessor.instance.username)
    const currentUser: Ref<UserDTO | undefined> = ref(LocalAccessor.instance.user)

    function setCurrentUsername(value: string) {
        currentUsername.value = value
        LocalAccessor.instance.username = value
    }

    function setCurrentUser(value: UserDTO) {
        currentUser.value = value
        LocalAccessor.instance.user = value
    }

    async function registerUser(registrationCode: string): Promise<void> {
        setCurrentUsername((await ServerAccessor.instance.createUser({registrationCode: registrationCode})).username)
        setCurrentUser((await ServerAccessor.instance.getUser(currentUsername.value!)))
    }

    async function loginUser(username: string): Promise<void> {
        setCurrentUsername(username)
        setCurrentUser((await ServerAccessor.instance.getUser(username)))
    }

    async function refreshUser(): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Ein Nutzer muss eingeloggt sein.")
        setCurrentUser((await ServerAccessor.instance.getUser(currentUsername.value)))
    }

    async function finishExerciseExecution(): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Ein Nutzer muss eingeloggt sein.")
        if(currentSubject.value === undefined) throw Error("Es ist kein Fach ausgewählt.")
        if(currentExercise.value === undefined) throw Error("Es ist keine Übung ausgewählt.")
        await ServerAccessor.instance.markExerciseOfSubjectOfUserAsSuccessful(currentUsername.value, currentSubject.value.id, currentExercise.value.id)
    }



    const allBooks: Ref<ReadonlyArray<BookDTO> | undefined> = ref(undefined)

    async function getBooks(): Promise<void> {
        allBooks.value = (await ServerAccessor.instance.getBooks()).books
    }



    const newSubjectName: Ref<string> = ref("")
    const newSubjectBookId: Ref<string | undefined> = ref(undefined)
    const newSubjectRegistrationCode: Ref<string | undefined> = ref(undefined)

    async function createSubject(): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        if(newSubjectBookId.value === undefined) throw Error("Es wurde kein Buch ausgewählt.")
        const registrationCode: RegistrationCodeDTO = await ServerAccessor.instance.createSubjectInUser(currentUsername.value, {name: newSubjectName.value, bookId: newSubjectBookId.value})
        newSubjectRegistrationCode.value = registrationCode.registrationCode
    }



    const subjects: Ref<ReadonlyArray<SubjectPartialDTO>|undefined> = ref(undefined)

    async function getSubjects(): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        subjects.value = (await ServerAccessor.instance.getSubjectsOfUser(currentUsername.value)).subjects
    }

    async function joinSubject(registrationCode: string): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        await ServerAccessor.instance.joinSubjectForUser(currentUsername.value, {registrationCode: registrationCode})
    }



    const currentSubject: Ref<SubjectDetailedDTO | undefined> = ref(undefined)
    const chaptersOfBookOfCurrentSubject: Ref<ReadonlyArray<string> | undefined> = ref(undefined)
    const exerciseTemplatesOfBookOfCurrentSubject: Ref<ReadonlyArray<ExerciseTemplatePartialDTO> | undefined> = ref(undefined)
    const exercisesOfBookOfCurrentSubject: Ref<ReadonlyArray<ExercisePartialDTO> | undefined> = ref(undefined)

    async function getSubject(subjectId: string): Promise<void> {
        if(currentUsername.value === undefined || currentUser.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        currentSubject.value = await ServerAccessor.instance.getSubjectOfUser(currentUsername.value, subjectId)
        chaptersOfBookOfCurrentSubject.value = (await ServerAccessor.instance.getChaptersOfBook(currentSubject.value.book.id)).chapters
        exerciseTemplatesOfBookOfCurrentSubject.value = (await ServerAccessor.instance.getExerciseTemplatesOfBook(currentSubject.value.book.id, null)).exerciseTemplates
        exercisesOfBookOfCurrentSubject.value = (await ServerAccessor.instance.getExercisesOfSubjectOfUser(currentUsername.value, subjectId)).exercises
    }



    const currentExercise: Ref<ExerciseDetailedDTO | undefined> = ref(undefined)

    async function getExercise(exerciseId: string): Promise<void> {
        if(currentUsername.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        if(currentSubject.value === undefined) throw Error("Es ist kein Fach ausgewählt.")
        currentExercise.value = await ServerAccessor.instance.getExerciseOfSubjectOfUser(currentUsername.value, currentSubject.value.id, exerciseId)
    }



    const newExerciseChapter: Ref<string|undefined> = ref(undefined)
    const newExerciseTemplateId: Ref<string|undefined> = ref(undefined)

    async function createExercise(exerciseCreation: ExerciseCreationDTO){
        if(currentUsername.value === undefined) throw Error("Es ist kein Nutzer eingeloggt.")
        if(currentSubject.value === undefined) throw Error("Es ist kein Fach ausgewählt.")
        if(newExerciseChapter.value === undefined) throw Error("Es wurde kein Kapitel ausgewählt.")
        if(newExerciseTemplateId.value === undefined) throw Error("Es wurde keine Übungsvorlage ausgewählt.")
        await ServerAccessor.instance.createExerciseInSubjectOfUser(currentUsername.value, currentSubject.value.id, exerciseCreation)
    }



    function reset() {
        currentUsername.value = undefined
        currentUser.value = undefined
        LocalAccessor.instance.reset()
        allBooks.value = undefined
        newSubjectName.value = ""
        newSubjectBookId.value = undefined
        newSubjectRegistrationCode.value = undefined
        subjects.value = undefined
        currentSubject.value = undefined
        chaptersOfBookOfCurrentSubject.value = undefined
        exerciseTemplatesOfBookOfCurrentSubject.value = undefined
        exercisesOfBookOfCurrentSubject.value = undefined
        currentExercise.value = undefined
        newExerciseChapter.value = undefined
        newExerciseTemplateId.value = undefined
    }



    return {
        currentUsername,
        currentUser,
        registerUser,
        loginUser,
        refreshUser,
        finishExerciseExecution,
        allBooks,
        getBooks,
        newSubjectName,
        newSubjectBookId,
        newSubjectRegistrationCode,
        createSubject,
        subjects,
        getSubjects,
        joinSubject,
        currentSubject,
        chaptersOfBookOfCurrentSubject,
        exerciseTemplatesOfBookOfCurrentSubject,
        exercisesOfBookOfCurrentSubject,
        getSubject,
        currentExercise,
        newExerciseChapter,
        newExerciseTemplateId,
        getExercise,
        createExercise,
        reset
    }
})