import { Role } from "./Role";

export interface UserViewDto {
    id: number,
    username: string,
    firstname: string, 
    lastname: string,
    managerId: number,
    roles: Set<Role>
}