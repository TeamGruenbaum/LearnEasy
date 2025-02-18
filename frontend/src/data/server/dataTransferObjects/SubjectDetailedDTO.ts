import type {BookDTO} from "@/data/server/dataTransferObjects/BookDTO.ts";

export type SubjectDetailedDTO = {
    readonly id: string
    readonly name: string
    readonly book: BookDTO
}