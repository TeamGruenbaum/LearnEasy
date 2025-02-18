import type {UserDTO} from "@/data/server/dataTransferObjects/users/UserDTO.ts";
import {z} from "zod";

export class LocalAccessor {
    static readonly instance: LocalAccessor = new LocalAccessor();

    private constructor() {}

    get username(): string | undefined {
        return localStorage.getItem("username") ?? undefined;
    }

    set username(value: string) {
        localStorage.setItem("username", value);
    }

    get user(): UserDTO | undefined {
        const localStorageValue = localStorage.getItem("user") ?? undefined;
        if(localStorageValue === undefined) return undefined;
        const user: unknown = JSON.parse(localStorageValue);
        const schemeCheckResult: z.SafeParseReturnType<unknown, UserDTO> = z.discriminatedUnion("type", [
            z.object({
                type: z.literal("TeacherDTO"),
            }),
            z.object({
                type: z.literal("PupilDTO"),
                exerciseProgresses: z.record(z.string(), z.boolean())
            })
        ]).safeParse(user)
        return schemeCheckResult.success ? schemeCheckResult.data : undefined;
    }

    set user(value: UserDTO) {
        localStorage.setItem("user", JSON.stringify(value));
    }

    get backendUrl(): string {
        if(localStorage.getItem("backendUrl") === null) localStorage.setItem("backendUrl", "http://localhost:8080")
        return localStorage.getItem("backendUrl")!
    }

    set backendUrl(value: string) {
        localStorage.setItem("backendUrl", value);
    }

    reset(): void {
        localStorage.removeItem("username");
        localStorage.removeItem("user");
        localStorage.removeItem("backendUrl");
    }
}