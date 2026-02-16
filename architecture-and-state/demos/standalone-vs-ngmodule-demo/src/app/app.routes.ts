import { Route } from './router';

export const routes: Route[] = [
  {
    path: 'home',
    kind: 'standalone',
    loadComponent: async () => new (await import('./features/home/home.component')).HomeComponent()
  },
  {
    path: 'legacy',
    kind: 'ngmodule',
    loadChildren: async () => new (await import('./features/legacy/legacy.module')).LegacyModule()
  }
];
