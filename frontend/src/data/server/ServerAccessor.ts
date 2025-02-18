import axios, {type AxiosInstance, type AxiosResponse} from "axios";
import {z} from "zod";
import type {SubjectsPageDTO} from "@/data/server/dataTransferObjects/SubjectsPageDTO.ts";
import type {BooksPageDTO} from "@/data/server/dataTransferObjects/BooksPageDTO.ts";
import type {SubjectCreationDTO} from "@/data/server/dataTransferObjects/SubjectCreationDTO.ts";
import type {RegistrationCodeDTO} from "@/data/server/dataTransferObjects/RegistrationCodeDTO.ts";
import type {SubjectDetailedDTO} from "@/data/server/dataTransferObjects/SubjectDetailedDTO.ts";
import type {ExercisesPageDTO} from "@/data/server/dataTransferObjects/ExercisesPageDTO.ts";
import type {UserDTO} from "@/data/server/dataTransferObjects/users/UserDTO.ts";
import type {ExerciseDetailedDTO} from "@/data/server/dataTransferObjects/exercises/ExerciseDetailedDTO.ts";
import {NotAllowedHttpStatusCodeError} from "@/data/server/errors/NotAllowedHttpStatusCodeError.ts";
import {ValidationError} from "@/data/server/errors/ValidationError.ts";
import type {
    ExerciseTemplatesPageDTO
} from "@/data/server/dataTransferObjects/ExerciseTemplatesPageDTO.ts";
import type {ChaptersPageDTO} from "@/data/server/dataTransferObjects/ChaptersPageDTO.ts";
import type {UsernameDTO} from "@/data/server/dataTransferObjects/UserNameDTO.ts";
import type {IdDTO} from "@/data/server/dataTransferObjects/IdDTO.ts";
import ExerciseCreation from "@/container/ExerciseCreation.vue";
import type {ExerciseCreationDTO} from "@/data/server/dataTransferObjects/exercises/ExerciseCreationDTO.ts";
import {LocalAccessor} from "@/data/LocalAccessor.ts";

export class ServerAccessor {
    static readonly instance: ServerAccessor = new ServerAccessor();

    client!: AxiosInstance

    private constructor() {
        this.setBackendUrl(LocalAccessor.instance.backendUrl)
    }

    setBackendUrl(url: string): void {
        this.client = axios.create({
            baseURL: `${url}/v1/`,
            timeout: 3000,
            responseType: "json",
            responseEncoding: "utf8",
            validateStatus: () => true
        });
    }

    async createUser(registrationCode: RegistrationCodeDTO): Promise<UsernameDTO> {
        return this.makeRequest(
            "POST",
            "users",
            undefined,
            registrationCode,
            [201],
            z.object({
                username: z.string()
            })
        )
    }

    async getUser(username: string): Promise<UserDTO> {
        return this.makeRequest(
            "GET",
            `users/${username}`,
            undefined,
            undefined,
            [200],
            z.discriminatedUnion("type", [
                z.object({
                    type: z.literal("TeacherDTO"),
                }),
                z.object({
                    type: z.literal("PupilDTO"),
                    exerciseProgresses: z.record(z.string(), z.boolean())
                })
            ])
        )
    }

    async getBooks(page: { pageNumber: number, perPage: number} | undefined = undefined): Promise<BooksPageDTO> {
        return this.makeRequest(
            "GET",
            "books",
            page !== undefined ? {pageNumber: page.pageNumber.toString(), perPage: page.perPage.toString()} : {pageNumber: null},
            undefined,
            [200],
            z.object({
                maximumPage: z.number(),
                books: z.array(
                    z.object({
                        id: z.string().uuid(),
                        isbn: z.string(),
                        title: z.string(),
                        publisher: z.string(),
                    })
                )
            })
        )
    }

    async getChaptersOfBook(bookId: string, page: { pageNumber: number, perPage: number} | undefined = undefined): Promise<ChaptersPageDTO> {
        return this.makeRequest(
            "GET",
            `/books/${bookId}/chapters`,
            page !== undefined ? {pageNumber: page.pageNumber.toString(), perPage: page.perPage.toString()} : {pageNumber: null},
            undefined,
            [200],
            z.object({
                maximumPage: z.number(),
                chapters: z.array(z.string())
            })
        )
    }

    async getExerciseTemplatesOfBook(bookId: string, chapter:string|null, page: { pageNumber: number, perPage: number} | undefined = undefined): Promise<ExerciseTemplatesPageDTO> {
        return this.makeRequest(
            "GET",
            `/books/${bookId}/exercise-templates`,
            page !== undefined ? {pageNumber: page.pageNumber.toString(), perPage: page.perPage.toString(), chapter: chapter} : {pageNumber: null, chapter: chapter},
            undefined,
            [200],
            z.object({
                maximumPage: z.number(),
                exerciseTemplates: z.array(
                    z.discriminatedUnion("type",[
                        z.object({
                            type: z.literal("VocabularyOfChapterExerciseTemplatePartialDTO"),
                            id: z.string(),
                            name: z.string(),
                            chapter: z.string()
                        })
                    ])
                )
            })
        )
    }

    async createSubjectInUser(username: string, subject: SubjectCreationDTO): Promise<RegistrationCodeDTO> {
        return this.makeRequest(
            "POST",
            `users/${username}/subjects`,
            undefined,
            subject,
            [201],
            z.object({
                registrationCode: z.string()
            })
        )
    }

    async joinSubjectForUser(username: string, registrationCode: RegistrationCodeDTO): Promise<void> {
        return this.makeRequest(
            "POST",
            `users/${username}/subjects/join`,
            undefined,
            registrationCode,
            [204],
            z.void()
        )
    }

    async getSubjectsOfUser(username: string, page: { pageNumber: number, perPage: number} | undefined = undefined): Promise<SubjectsPageDTO> {
        return this.makeRequest(
            "GET",
            `users/${username}/subjects`,
            page !== undefined ? {pageNumber: page.pageNumber.toString(), perPage: page.perPage.toString()} : {pageNumber: null},
            undefined,
            [200],
            z.object({
                maximumPage: z.number(),
                subjects: z.array(
                    z.object({
                        id: z.string(),
                        name: z.string()
                    })
                )
            })
        )
    }

    async getSubjectOfUser(username: string, subjectId: string): Promise<SubjectDetailedDTO> {
        return this.makeRequest(
            "GET",
            `users/${username}/subjects/${subjectId}`,
            undefined,
            undefined,
            [200],
            z.object({
                id: z.string(),
                name: z.string(),
                book: z.object({
                    id: z.string(),
                    isbn: z.string(),
                    title: z.string(),
                    publisher: z.string()
                })
            })
        )
    }

    async createExerciseInSubjectOfUser(username: string, subjectId: string, exerciseCreation: ExerciseCreationDTO): Promise<IdDTO> {
        return this.makeRequest(
            "POST",
            `users/${username}/subjects/${subjectId}/exercises`,
            undefined,
            exerciseCreation,
            [201],
            z.object({
                id: z.string()
            })
        )
    }

    async getExercisesOfSubjectOfUser(username: string, subjectId: string, page: { pageNumber: number, perPage: number} | undefined = undefined): Promise<ExercisesPageDTO> {
        return this.makeRequest(
            "GET",
            `users/${username}/subjects/${subjectId}/exercises`,
            page !== undefined ? {pageNumber: page.pageNumber.toString(), perPage: page.perPage.toString()} : {pageNumber: null},
            undefined,
            [200],
            z.object({
                maximumPage: z.number(),
                exercises: z.array(
                    z.discriminatedUnion("type",[
                        z.object({
                            type: z.literal("VocabularyOfChapterExercisePartialDTO"),
                            id: z.string(),
                            vocabularyOfChapterExerciseTemplate: z.object({
                                type: z.literal("VocabularyOfChapterExerciseTemplatePartialDTO"),
                                id: z.string(),
                                name: z.string(),
                                chapter: z.string()
                            })
                        })
                    ])
                )
            })
        )
    }

    async getExerciseOfSubjectOfUser(username: string, subjectId: string, exerciseId: string): Promise<ExerciseDetailedDTO> {
        return this.makeRequest(
            "GET",
            `users/${username}/subjects/${subjectId}/exercises/${exerciseId}`,
            undefined,
            undefined,
            [200],
            z.object ({
                type: z.literal("VocabularyOfChapterExerciseDetailedDTO"),
                id: z.string(),
                presentation: z.union([
                    z.literal("TextAskForeignLanguage"),
                    z.literal("TextAskNativeLanguage"),
                    z.literal("TextRandom"),
                    z.literal("Pairs"),
                ]),
                selectedVocabularyWordNumbers: z.union([z.array(z.number()), z.null()]),
                numberOfCorrectAnswersForCompletion: z.number(),
                vocabularyOfChapterExerciseTemplate: z.object({
                    type: z.literal("VocabularyOfChapterExerciseTemplateDetailedDTO"),
                    id: z.string(),
                    name: z.string(),
                    chapter: z.string(),
                    vocabulary: z.array(
                        z.object({
                            id: z.string(),
                            foreignLanguage: z.string(),
                            nativeLanguage: z.string(),
                            chapter: z.string(),
                            number: z.number(),
                            image: z.union([z.string(), z.null()]),
                            audio: z.union([z.string(), z.null()])
                        })
                    )
                })
            })
        )
    }

    async markExerciseOfSubjectOfUserAsSuccessful(username: string, subjectId: string, exerciseId: string): Promise<void>{
        await this.makeRequest(
            "POST",
            `users/${username}/subjects/${subjectId}/exercises/${exerciseId}/success`,
            undefined,
            undefined,
            [204],
            z.void()
        )
    }

    private async makeRequest<T>(
        method: "GET" | "POST" | "DELETE",
        url: string,
        queryParameter: Record<string, string | null> | undefined = undefined,
        body: object | undefined = undefined,
        expectedStatusCodes: Array<number>,
        expectedDataScheme: z.ZodType<T>
    ): Promise<T> {
        let response: AxiosResponse<unknown> = await this.client.request(
            {
                method: method,
                url: url,
                params: queryParameter,
                data: body,
                headers: { "Content-Type": "application/json" }
            }
        )
        if(response.data === "") response.data = undefined

        if (!expectedStatusCodes.includes(response.status)) throw new NotAllowedHttpStatusCodeError(response.status, response.data)

        const schemeCheckResult: z.SafeParseReturnType<T, T> = expectedDataScheme.safeParse(response.data)
        if (!schemeCheckResult.success) throw new ValidationError(schemeCheckResult.error)

        return schemeCheckResult.data
    }
}

