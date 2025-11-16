import { inject } from '@angular/core';
import { CanActivateFn, RedirectCommand, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';

export const adminPanelGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  if (!authService.checkRole(authService.user, "ADMIN")) {
    const router = inject(Router);
    const homePath = router.parseUrl("/");
    return new RedirectCommand(homePath);
  } else
    return true;
};
