export type VocabularyWordDTO = {
    readonly id: string
    readonly foreignLanguage: string
    readonly nativeLanguage: string
    readonly chapter: string
    readonly number: number
    readonly image: string | null
    readonly audio: string | null
}