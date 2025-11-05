import { HttpClient, HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from './environment';
import { LoginRequestDto } from './login-signup-page/model/LoginRequestDto';
import { SignupRequestDto } from './login-signup-page/model/SignupRequestDto';
import { LoginResponseDto } from './login-signup-page/model/LoginResponseDto';
import { UserViewDto } from './model/UserViewDto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  http = inject(HttpClient);
  router = inject(Router);
  isAuthenticated = signal(false);

  user : UserViewDto | null = null;
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
}


// export function authorizationInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
//   const authToken = inject(AuthService).userToken();

//   console.log(authToken)
//   if (!authToken) return next(req);
//   if (req.url.includes("signInWithPassword")) return next(req);

//   const newReq = req.clone({
//     headers: req.headers.append('Authorization', `Bearer ${authToken}`)
//   });

//   return next(newReq);
// }