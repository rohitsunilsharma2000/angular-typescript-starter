// Minimal Angular core typings so the examples compile without installing @angular/core
declare module '@angular/core' {
  export function Injectable(opts: any): ClassDecorator;
  export function signal<T>(v: T): { (): T; set(value: T): void };
  export function computed<T>(fn: () => T): { (): T };
}
