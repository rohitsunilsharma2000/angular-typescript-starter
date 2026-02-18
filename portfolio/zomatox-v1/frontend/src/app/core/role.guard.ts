import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserContextService } from './user-context.service';

export const ownerGuard: CanActivateFn = () => {
  const uc = inject(UserContextService);
  const router = inject(Router);

  if (uc.role === 'OWNER') {
    return true;
  }

  return router.createUrlTree(['/restaurants']);
};

export const deliveryGuard: CanActivateFn = () => {
  const uc = inject(UserContextService);
  const router = inject(Router);

  if (uc.role === 'DELIVERY_PARTNER') {
    return true;
  }

  return router.createUrlTree(['/restaurants']);
};
