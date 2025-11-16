
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../service/auth-service';

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
      // TODO: what's the error we get when token timed out? 403? 401?
      if (err.status === 401) {
        authService.logout();
        authService.router.navigateByUrl("/login");
      }
      return throwError(() => err);
    })
  );
}
