import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <header class="bg-white/80 backdrop-blur sticky top-0 border-b border-slate-200">
      <nav class="max-w-6xl mx-auto px-4 py-3 flex gap-4">
        <a routerLink="/" class="font-semibold text-brand-700">BlinkIt Mega</a>
        <a routerLink="/customer/home">Customer</a>
        <a routerLink="/admin/products">Admin</a>
        <a routerLink="/warehouse/picking-queue">Warehouse</a>
        <a routerLink="/rider/batches">Rider</a>
      </nav>
    </header>
    <main class="max-w-6xl mx-auto px-4 py-6">
      <router-outlet></router-outlet>
    </main>
  `
})
export class AppComponent {}
