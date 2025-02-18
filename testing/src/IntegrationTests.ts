import { describe, it } from "mocha";
import * as chai from "chai";
import { assert } from "chai";
import chaiUuid from "chai-uuid";
import axios, {type AxiosInstance, type AxiosResponse, HttpStatusCode} from "axios";



chai.use(chaiUuid);



const backenUrl: string | undefined = process.env.LEARNEASY_BACKEND_URL
if (backenUrl === undefined) throw new Error("Environment variable LEARNEASY_BACKEND_URL is not set");

const httpClient: AxiosInstance = axios.create({
    baseURL: `${backenUrl}`,
    timeout: 3000,
    responseType: "json",
    responseEncoding: "utf8",
    validateStatus: () => true
});



const schoolRegistrationCode: string | undefined = process.env.LEARNEASY_SCHOOL_REGISTRATION_CODE
if (schoolRegistrationCode === undefined) throw new Error("Environment variable LEARNEASY_SCHOOL_REGISTRATION_CODE is not set");

let teacherUsername: string | undefined = undefined;
let pupilUsername: string | undefined = undefined;

let newSubjectRegistrationCode: string | undefined = undefined;
let newSubjectBook: any = undefined;
let newSubjectId: string | undefined = undefined;

let newExerciseId: string | undefined = undefined;



describe("Integration Tests", async () => {
    it("Teacher - Register ", async () => {
        const createUserResponse: AxiosResponse<any, any> = await httpClient.post(
            "/v1/users",
            {
                registrationCode: schoolRegistrationCode
            },
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(createUserResponse.status, HttpStatusCode.Created)
        assert.typeOf(createUserResponse.data.username, "string")
        assert.isAtLeast(createUserResponse.data.username.length, 10)
        teacherUsername = createUserResponse.data.username;

        const getUserResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${teacherUsername}`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(getUserResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getUserResponse.data.type, "TeacherDTO")
    }).slow(3000);

    it("Teacher - Create subject", async () => {
        const getBooksResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/books`,
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                }
            }
        );
        assert.strictEqual(getBooksResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getBooksResponse.data.maximumPage, 1)
        assert.lengthOf(getBooksResponse.data.books, 1)
        assert.uuid(getBooksResponse.data.books[0].id)
        assert.deepEqual(
            getBooksResponse.data.books[0],
            {
                id: getBooksResponse.data.books[0].id,
                isbn: "978-3061214302",
                title: "À plus ! - Französisch als 1. und 2. Fremdsprache - Bayern - Ausgabe 2017",
                publisher: "Cornelsen Verlag",
            }
        )
        newSubjectBook = getBooksResponse.data.books[0];

        const createSubjectResponse: AxiosResponse<any, any> = await httpClient.post(
            `/v1/users/${teacherUsername}/subjects`,
            {
                name: "Französisch 9A",
                bookId: newSubjectBook.id
            },
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                },
            }
        );
        assert.strictEqual(createSubjectResponse.status, HttpStatusCode.Created)
        assert.typeOf(createSubjectResponse.data.registrationCode, "string")
        assert.lengthOf(createSubjectResponse.data.registrationCode, 6)
        newSubjectRegistrationCode = createSubjectResponse.data.registrationCode;

        const getSubjectsResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${teacherUsername}/subjects`,
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                },
            }
        );
        assert.strictEqual(getSubjectsResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getSubjectsResponse.data.maximumPage, 1)
        assert.lengthOf(getSubjectsResponse.data.subjects, 1)
        assert.uuid(getSubjectsResponse.data.subjects[0].id)
        assert.deepEqual(getSubjectsResponse.data.subjects[0], {
            id: getSubjectsResponse.data.subjects[0].id,
            name: "Französisch 9A",
        })
        newSubjectId = getSubjectsResponse.data.subjects[0].id;

        const getSubjectResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${teacherUsername}/subjects/${newSubjectId}`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(getSubjectResponse.status, HttpStatusCode.Ok)
        assert.deepEqual(getSubjectResponse.data, {
            id: newSubjectId,
            name: "Französisch 9A",
            book: newSubjectBook
        })
    }).slow(3000);

    it("Teacher - Create vocabulary exercise", async () => {
        const bookExerciseTemplatesResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/books/${newSubjectBook.id}/exercise-templates`,
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                }
            }
        );
        assert.strictEqual(bookExerciseTemplatesResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(bookExerciseTemplatesResponse.data.maximumPage, 1)
        assert.uuid(bookExerciseTemplatesResponse.data.exerciseTemplates[0].id)
        assert.uuid(bookExerciseTemplatesResponse.data.exerciseTemplates[1].id)
        assert.deepEqual(
            bookExerciseTemplatesResponse.data.exerciseTemplates,
            [
            {
                type: "VocabularyOfChapterExerciseTemplatePartialDTO",
                id: bookExerciseTemplatesResponse.data.exerciseTemplates[0].id,
                name: "Vokabelübung Unité 1",
                chapter: "Unité 1 La rentrée"
            },
            {
                type: "VocabularyOfChapterExerciseTemplatePartialDTO",
                id: bookExerciseTemplatesResponse.data.exerciseTemplates[1].id,
                name: "Vokabelübung Unité 2",
                chapter: "Unité 2 À la maison"
            }
        ])
        const exerciseTemplateId: string = bookExerciseTemplatesResponse.data.exerciseTemplates[0].id;

        const createExerciseResponse: AxiosResponse<any, any> = await httpClient.post(
            `/v1/users/${teacherUsername}/subjects/${newSubjectId}/exercises`,
            {
                type: "VocabularyOfChapterExerciseCreationDTO",
                exerciseTemplateId: exerciseTemplateId
            },
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(createExerciseResponse.status, HttpStatusCode.Created)
        assert.uuid(createExerciseResponse.data.id)
        newExerciseId = createExerciseResponse.data.id;

        const getExercisesResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${teacherUsername}/subjects/${newSubjectId}/exercises`,
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                }
            }
        );
        assert.strictEqual(getExercisesResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getExercisesResponse.data.maximumPage, 1)
        assert.lengthOf(getExercisesResponse.data.exercises, 1)
        assert.uuid(getExercisesResponse.data.exercises[0].vocabularyOfChapterExerciseTemplate.id)
        assert.deepEqual(getExercisesResponse.data.exercises[0], {
            id: newExerciseId,
            type: "VocabularyOfChapterExercisePartialDTO",
            vocabularyOfChapterExerciseTemplate: {
                id: getExercisesResponse.data.exercises[0].vocabularyOfChapterExerciseTemplate.id,
                type: "VocabularyOfChapterExerciseTemplatePartialDTO",
                name: "Vokabelübung Unité 1",
                chapter: "Unité 1 La rentrée"
            }
        })
    }).slow(3000);

    it("Pupil - Register and thereby join subject", async () => {
        const createUserResponse: AxiosResponse<any, any> = await httpClient.post(
            "/v1/users",
            {
                registrationCode: newSubjectRegistrationCode
            },
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(createUserResponse.status, HttpStatusCode.Created)
        assert.typeOf(createUserResponse.data.username, "string")
        assert.isAtLeast(createUserResponse.data.username.length, 10)
        pupilUsername = createUserResponse.data.username;

        const getUserResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${pupilUsername}`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(getUserResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getUserResponse.data.type, "PupilDTO")

        const getSubjectsResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${pupilUsername}/subjects`,
            {
                headers: {
                    "Content-Type": "application/json"
                },
                params: {
                    pageNumber: null
                }
            }
        );
        assert.strictEqual(getSubjectsResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getSubjectsResponse.data.maximumPage, 1)
        assert.lengthOf(getSubjectsResponse.data.subjects, 1)
        assert.uuid(getSubjectsResponse.data.subjects[0].id)

        assert.strictEqual(getSubjectsResponse.data.subjects[0].id, newSubjectId)
    }).slow(3000);

    it("Pupil - Do vocabulary exercise successfully", async () => {
        const getExerciseResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${pupilUsername}/subjects/${newSubjectId}/exercises/${newExerciseId}`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(getExerciseResponse.status, HttpStatusCode.Ok)
        assert.uuid(getExerciseResponse.data.vocabularyOfChapterExerciseTemplate.id)

        assert.match(
            JSON.stringify(getExerciseResponse.data),
            new RegExp(`\\{"type":"VocabularyOfChapterExerciseDetailedDTO","id":"${newExerciseId}","vocabularyOfChapterExerciseTemplate":\\{"type":"VocabularyOfChapterExerciseTemplateDetailedDTO","id":"${getExerciseResponse.data.vocabularyOfChapterExerciseTemplate.id}","name":"Vokabelübung Unité 1","chapter":"Unité 1 La rentrée","vocabulary":\\[.*?\\]\\},"presentation":"TextAskForeignLanguage","selectedVocabularyWordNumbers":\\[\\],"numberOfCorrectAnswersForCompletion":10\\}`))

        const markExerciseAsSuccessfulResponse: AxiosResponse<any, any> = await httpClient.post(
            `/v1/users/${pupilUsername}/subjects/${newSubjectId}/exercises/${newExerciseId}/success`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(markExerciseAsSuccessfulResponse.status, HttpStatusCode.NoContent)

        const getUserResponse: AxiosResponse<any, any> = await httpClient.get(
            `/v1/users/${pupilUsername}`,
            {
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
        assert.strictEqual(getUserResponse.status, HttpStatusCode.Ok)
        assert.strictEqual(getUserResponse.data.type, "PupilDTO")

        if(typeof newExerciseId === "string")
        {
            assert.deepEqual(getUserResponse.data.exerciseProgresses, {
                [newExerciseId]: true
            })
        }
        else assert.fail("Value of newExerciseId is invalid.")

    }).slow(3000);
})