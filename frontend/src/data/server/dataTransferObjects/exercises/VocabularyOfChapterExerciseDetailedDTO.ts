import type {PresentationDTO} from "@/data/server/dataTransferObjects/PresentationDTO.ts";
import type {VocabularyOfChapterExerciseTemplateDetailedDTO} from "@/data/server/dataTransferObjects/exercises/exerciseTemplates/VocabularyOfChapterExerciseTemplateDetailedDTO.ts";

export type VocabularyOfChapterExerciseDetailedDTO = {id: string} &  {
    readonly type: "VocabularyOfChapterExerciseDetailedDTO"
    readonly vocabularyOfChapterExerciseTemplate: VocabularyOfChapterExerciseTemplateDetailedDTO
    readonly presentation: PresentationDTO
    readonly selectedVocabularyWordNumbers: number[] | null
    readonly numberOfCorrectAnswersForCompletion: number
}