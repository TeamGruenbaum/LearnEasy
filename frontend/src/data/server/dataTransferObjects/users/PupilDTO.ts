import type {UserDTO} from "@/data/server/dataTransferObjects/users/UserDTO.ts";

export type PupilDTO = {
    readonly type: "PupilDTO",
    readonly exerciseProgresses: Record<string, boolean>
}