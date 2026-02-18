import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { CartStore } from '../../core/cart.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Checkout</h1>

  <div class="bg-white border rounded-xl p-4">
    <div class="text-sm text-slate-600 mb-3">
      v1 demo: choose addressId manually (seeded addresses exist for userId=1).
      Try addressId: 1 or 2
    </div>

    <div class="flex items-center gap-2">
      <input class="border rounded px-3 py-2 w-40" type="number" [value]="addressId()" (input)="addressId.set(+$any($event.target).value)" />
      <button class="bg-black text-white rounded px-4 py-2" (click)="place()">Place Order</button>
    </div>

    <div *ngIf="error()" class="mt-3 text-red-600 text-sm">{{error()}}</div>
  </div>
  `,
})
export class CheckoutComponent {
  private api = inject(ApiService);
  private cart = inject(CartStore);
  private router = inject(Router);

  addressId = signal<number>(1);
  error = signal<string>('');

  place() {
    this.error.set('');
    this.api.createOrder(this.addressId()).subscribe({
      next: (o) => {
        this.cart.load();
        this.router.navigate(['/orders', o.id]);
      },
      error: (e) => this.error.set(e?.error?.message ?? 'Failed'),
    });
  }
}
