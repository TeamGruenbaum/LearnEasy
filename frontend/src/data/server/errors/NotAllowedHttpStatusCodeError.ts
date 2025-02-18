export class NotAllowedHttpStatusCodeError extends Error {
    readonly statusCode: number;
    readonly body: unknown;

    constructor(statusCode: number, body: unknown) {
        super(`Ein nicht erlaubter Status Code (${statusCode}) wurde erhalten.`);
        this.name = "NotAllowedHttpStatusError";
        this.statusCode = statusCode;
        this.body = body;
    }
}