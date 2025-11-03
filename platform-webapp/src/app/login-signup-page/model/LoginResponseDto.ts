import { UserViewDto } from "../../model/UserViewDto";

export interface LoginResponseDto {
    token: string,
    user: UserViewDto
}