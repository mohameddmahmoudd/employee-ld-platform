import { Role } from "./Role";

export interface UserDto {
    id: number,
    username: string,
    fullName: string,
    title: string,
    managerId: number,
    roles: string[]//("EMPLOYEE" | "GUEST" | "ADMIN" | "MANAGER")[];
}