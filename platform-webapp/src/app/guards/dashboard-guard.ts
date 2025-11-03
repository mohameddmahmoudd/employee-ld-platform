import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { AuthService } from '../auth-service';

export const dashboardGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  if (!authService.isAuthenticated()) {
    const router = inject(Router);
    const loginPath = router.parseUrl("/login");
    return new RedirectCommand(loginPath);
  } else
    return true;
};

