export type StandaloneComponent = { render(): string };
export type NgModule = { getRootComponent(): StandaloneComponent };
export type Route =
  | { path: string; kind: 'standalone'; loadComponent: () => Promise<StandaloneComponent> }
  | { path: string; kind: 'ngmodule'; loadChildren: () => Promise<NgModule> };

export async function navigate(routes: Route[], path: string): Promise<string> {
  const route = routes.find(r => r.path === path);
  if (!route) return '404: unknown route';

  if (route.kind === 'standalone') {
    const cmp = await route.loadComponent();
    return `[standalone] ${cmp.render()}`;
  }

  const mod = await route.loadChildren();
  return `[ngmodule] ${mod.getRootComponent().render()}`;
}
