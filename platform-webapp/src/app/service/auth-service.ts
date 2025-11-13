import { HttpClient, HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../environment';
import { LoginRequestDto } from '../login-signup-page/model/LoginRequestDto';
import { SignupRequestDto } from '../login-signup-page/model/SignupRequestDto';
import { LoginResponseDto } from '../login-signup-page/model/LoginResponseDto';
import { UserDto } from '../model/UserDto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  http = inject(HttpClient);
  router = inject(Router);
  isAuthenticated = signal(false);

  user : UserDto | null = null;
  userToken : string | null = null;

  login(dto: LoginRequestDto) {
    let postEndpoint = environment.AUTH_API_URL + "/login";
    return this.http.post<LoginResponseDto>(postEndpoint, dto);
  }

  signup(dto: SignupRequestDto) {
    let postEndpoint = environment.AUTH_API_URL + "/signup";
    return this.http.post<LoginResponseDto>(postEndpoint, dto);
  }

  logout(){
    this.isAuthenticated.set(false);
    this.user = null;
    this.userToken = null;
  }

  checkRole(user: UserDto | null, role: String){
    if (!user) return false;
    return [...user.roles].some(r => r.name === role);
  }
}


import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export function authorizationInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const authService = inject(AuthService);
  const authToken = authService.userToken;

  const cloned = authToken
    ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${authToken}`) })
    : req;

  return next(cloned).pipe(
    tap(event => {
      // handle successful responses here TODO?
    }),
    catchError(err => {
      // TODO: what's the error we get when token times out? 403? 401?
      if (err.status === 401) {
        authService.logout();
        authService.router.navigateByUrl("/login");
      }
      return throwError(() => err);
    })
  );
}
