import type {SubjectPartialDTO} from "@/data/server/dataTransferObjects/SubjectPartialDTO.ts";

export type SubjectsPageDTO = {
    readonly maximumPage: number
    readonly subjects: ReadonlyArray<SubjectPartialDTO>
}