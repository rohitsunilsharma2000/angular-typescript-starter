import { Injectable, signal } from '@angular/core';
import { Cart } from './models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CartStore {
  cart = signal<Cart | null>(null);
  loading = signal(false);

  constructor(private api: ApiService) {}

  load() {
    this.loading.set(true);
    this.api.getCart().subscribe({
      next: (c) => this.cart.set(c),
      error: () => this.cart.set(null),
      complete: () => this.loading.set(false),
    });
  }

  upsert(menuItemId: number, qty: number) {
    this.api.upsertCartItem(menuItemId, qty).subscribe((c) => this.cart.set(c));
  }

  remove(menuItemId: number) {
    this.api.removeCartItem(menuItemId).subscribe((c) => this.cart.set(c));
  }
}
