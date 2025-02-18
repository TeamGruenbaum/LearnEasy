import type {ExerciseTemplatePartialDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/ExerciseTemplatePartialDTO.ts";

export type ExerciseTemplatesPageDTO = {
    readonly maximumPage: number
    readonly exerciseTemplates: ReadonlyArray<ExerciseTemplatePartialDTO>
}