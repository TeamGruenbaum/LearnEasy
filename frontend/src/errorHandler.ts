import type {ComponentPublicInstance} from "@vue/runtime-core";
import {AxiosError} from "axios";
import {Dialog} from "@/components/Dialog.ts";
import {NotAllowedHttpStatusCodeError} from "@/data/server/errors/NotAllowedHttpStatusCodeError.ts";
import {ValidationError} from "@/data/server/errors/ValidationError.ts";
import {useStore} from "@/store.ts";

export const errorHandler = async (error: unknown, instance: ComponentPublicInstance | null, info: string) => {
    if(error instanceof AxiosError && error.code === "ERR_NETWORK") return "Es konnte keine Verbindung zum Server hergestellt werden."
    else if(error instanceof NotAllowedHttpStatusCodeError && error.statusCode === 400) {
        console.error(error.body)
        await Dialog.Common.errorOccurred("Die Anfrage ist fehlerhaft.").show()
    }
    else if(error instanceof NotAllowedHttpStatusCodeError && error.statusCode === 403) {
        console.error(error.body)
        await Dialog.Common.errorOccurred("Diese Aktion ist nicht erlaubt.").show()
    }
    else if(error instanceof NotAllowedHttpStatusCodeError && error.statusCode === 404) {
        console.error(error.body)
        await Dialog.Common.errorOccurred("Die Daten konnten nicht gefunden werden.").show()
    }
    else if(error instanceof NotAllowedHttpStatusCodeError && error.statusCode === 500) {
        console.error(error.body)
        await Dialog.Common.errorOccurred("Ein interner Serverfehler ist aufgetreten.").show()
    }
    else if(error instanceof ValidationError) {
        console.error(error.error)
        await Dialog.Common.errorOccurred("Der Server hat fehlerhafte Daten gesendet.").show()
    }
    else if(error instanceof Error) {
        console.error(error)
        await Dialog.Common.errorOccurred("Ein unerwarteter Fehler ist aufgetreten.").show()
    }
    else {
        console.error(error)
        await Dialog.Common.errorOccurred("Ein unerwarteter Fehler ist aufgetreten.").show()
    }
}