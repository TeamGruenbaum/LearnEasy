import {z} from "zod";

export class ValidationError extends Error {
    readonly error: z.ZodError;

    constructor(error: z.ZodError) {
        super("Eine Validierung schlug fehl.");
        this.name = "ValidationError";
        this.error = error;
    }
}