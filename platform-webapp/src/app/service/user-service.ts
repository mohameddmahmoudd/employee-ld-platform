import { inject, Injectable } from '@angular/core';
import { environment } from '../environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserDto } from '../model/UserDto';
import { Role } from '../model/Role';
import { UserUpdateInfoDTO } from '../home/admin-panel-component/UserUpdateInfoDto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  http = inject(HttpClient);

  getUser(id: number) {
    const url = `${environment.USERS_API_URL}/id/${id}`;
    return this.http.get<UserDto>(url);
  }
  getUserByUsername(username: string) {
    const url = `${environment.USERS_API_URL}/username/${username}`;
    return this.http.get<UserDto>(url);
  }
  updateUserInfo(id: number, dto: UserUpdateInfoDTO) {
    const url = `${environment.USERS_API_URL}/${id}`;
    return this.http.put<UserDto>(url, dto);
  }
  updateUserRoles(id: number, roles: string[]) {
    const url = `${environment.USERS_API_URL}/${id}/role`;
    return this.http.patch<string[]>(url, roles);
  }
  fetchDefaultRoles() {
    const url = `${environment.USERS_API_URL}/roles`;
    return this.http.get<string[]>(url);
  }
  updateUserManager(id: number, managerId: number | null) {
    const url = `${environment.USERS_API_URL}/${id}/managerId`;
    const params = { params: new HttpParams().set('managerId', managerId? managerId : 0) };
    return this.http.patch<void>(url, null, params);
  }

}

