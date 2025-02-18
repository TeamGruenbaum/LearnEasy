import type {ExercisePartialDTO} from "@/data/server/dataTransferObjects/exercises/ExercisePartialDTO.ts";

export type ExercisesPageDTO = {
    readonly maximumPage: number
    readonly exercises: ReadonlyArray<ExercisePartialDTO>
}