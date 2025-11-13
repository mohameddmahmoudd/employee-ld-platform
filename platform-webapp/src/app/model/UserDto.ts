import { Role } from "./Role";

export interface UserDto {
    id: number,
    username: string,
    fullName: string,
    title: string,
    managerId: number,
    roles: Set<Role> //Set<"EMPLOYEE" | "GUEST" | "ADMIN" | "MANAGER">//
}