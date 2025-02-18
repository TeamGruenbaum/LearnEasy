import type {VocabularyOfChapterExerciseTemplatePartialDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/VocabularyOfChapterExerciseTemplatePartialDTO.ts";
import type {ExercisePartialDTO} from "@/data/server/dataTransferObjects/exercises/ExercisePartialDTO.ts";

export type VocabularyOfChapterExercisePartialDTO = {id: string} & {
    readonly type: "VocabularyOfChapterExercisePartialDTO"
    readonly vocabularyOfChapterExerciseTemplate: VocabularyOfChapterExerciseTemplatePartialDTO
}