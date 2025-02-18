import type {ExerciseTemplateDetailedDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/ExerciseTemplateDetailedDTO.ts";
import type {VocabularyWordDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/VocabularyWordDTO.ts";

export type VocabularyOfChapterExerciseTemplateDetailedDTO = {id: string, name: string, chapter: string} & {
    readonly type: "VocabularyOfChapterExerciseTemplateDetailedDTO"
    readonly vocabulary: ReadonlyArray<VocabularyWordDTO>
}