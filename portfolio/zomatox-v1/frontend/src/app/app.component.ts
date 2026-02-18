import { CommonModule } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthStore } from './core/auth.store';
import { AuthService } from './core/auth.service';
import { CartStore } from './core/cart.store';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
  <div class="min-h-screen">
    <header class="sticky top-0 z-10 bg-white border-b">
      <div class="max-w-5xl mx-auto px-4 py-3 flex items-center gap-4">
        <a routerLink="/login" class="font-bold text-lg">ZomatoX</a>

        <nav class="flex gap-3 text-sm" *ngIf="isLoggedIn()">
          <ng-container [ngSwitch]="role()">
            <ng-container *ngSwitchCase="'CUSTOMER'">
              <a routerLink="/restaurants" class="hover:underline">Restaurants</a>
              <a routerLink="/cart" class="hover:underline">Cart</a>
              <a routerLink="/orders" class="hover:underline">Orders</a>
              <a routerLink="/customer/addresses" class="hover:underline">Addresses</a>
              <a routerLink="/customer/favorites" class="hover:underline">Favorites</a>
            </ng-container>
            <ng-container *ngSwitchCase="'OWNER'">
              <a routerLink="/owner/restaurants" class="hover:underline">My Restaurants</a>
              <a routerLink="/owner/orders" class="hover:underline">Owner Orders</a>
            </ng-container>
            <ng-container *ngSwitchCase="'DELIVERY_PARTNER'">
              <a routerLink="/delivery/jobs" class="hover:underline">Delivery Jobs</a>
            </ng-container>
            <ng-container *ngSwitchCase="'ADMIN'">
              <a routerLink="/admin/restaurants" class="hover:underline">Admin Restaurants</a>
              <a routerLink="/admin/reviews" class="hover:underline">Admin Reviews</a>
            </ng-container>
          </ng-container>
        </nav>

        <div class="ml-auto flex items-center gap-3">
          <div class="text-xs text-slate-600" *ngIf="user()">
            {{user()!.email}} • {{user()!.role}}
          </div>

          <div class="text-sm bg-slate-100 rounded px-2 py-1" *ngIf="role()==='CUSTOMER'">
            Cart: {{cartCount()}}
          </div>

          <button *ngIf="isLoggedIn()" class="border rounded px-2 py-1 text-sm" (click)="logout()">Logout</button>
        </div>
      </div>
    </header>

    <main class="max-w-5xl mx-auto px-4 py-6">
      <router-outlet></router-outlet>
    </main>
  </div>
  `,
})
export class AppComponent {
  private store = inject(AuthStore);
  private auth = inject(AuthService);
  private cartStore = inject(CartStore);
  private router = inject(Router);

  user = this.store.user;
  role = this.store.role;
  isLoggedIn = this.store.isLoggedIn;
  cartCount = computed(() => this.cartStore.cart()?.items?.reduce((a, x) => a + x.qty, 0) ?? 0);

  constructor() {
    if (this.role() === 'CUSTOMER') this.cartStore.load();
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
