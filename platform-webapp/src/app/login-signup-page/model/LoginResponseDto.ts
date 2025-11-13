import { UserDto } from "../../model/UserDto";

export interface LoginResponseDto {
    token: string,
    user: UserDto
}