import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthStore } from '../auth/auth.store';

export const roleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthStore);
  const router = inject(Router);
  const roles = (route.data?.['roles'] as string[]) ?? [];
  if (roles.length === 0 || roles.includes(auth.role())) {
    return true;
  }
  return router.createUrlTree(['/']);
};
