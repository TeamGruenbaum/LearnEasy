import {type PresentationDTO} from "@/data/server/dataTransferObjects/PresentationDTO.ts";

export type VocabularyOfChapterExerciseCreationDTO = { exerciseTemplateId: string } & {
    type: "VocabularyOfChapterExerciseCreationDTO"
    presentation: PresentationDTO | null
    selectedVocabularyWordNumbers: number[] | null
    numberOfCorrectAnswersForCompletion: number | null
}