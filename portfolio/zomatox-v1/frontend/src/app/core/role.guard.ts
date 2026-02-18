import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthStore } from './auth.store';

const hasRole = (expected: string[]): boolean => {
  const store = inject(AuthStore);
  return !!store.role() && expected.includes(store.role()!);
};

export const authGuard: CanActivateFn = () => {
  const store = inject(AuthStore);
  const router = inject(Router);
  return store.isLoggedIn() ? true : router.createUrlTree(['/login']);
};

export const customerGuard: CanActivateFn = () => {
  const router = inject(Router);
  return hasRole(['CUSTOMER']) ? true : router.createUrlTree(['/login']);
};

export const ownerGuard: CanActivateFn = () => {
  const router = inject(Router);
  return hasRole(['OWNER']) ? true : router.createUrlTree(['/login']);
};

export const deliveryGuard: CanActivateFn = () => {
  const router = inject(Router);
  return hasRole(['DELIVERY_PARTNER']) ? true : router.createUrlTree(['/login']);
};

export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  return hasRole(['ADMIN']) ? true : router.createUrlTree(['/login']);
};
