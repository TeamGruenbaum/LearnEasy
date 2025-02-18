import type {BookDTO} from "@/data/server/dataTransferObjects/BookDTO.ts";

export type BooksPageDTO = {
    readonly maximumPage: number
    readonly books: ReadonlyArray<BookDTO>
}