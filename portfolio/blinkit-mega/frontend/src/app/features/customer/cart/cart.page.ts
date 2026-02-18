import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Cart</h2>
      <div class="mt-4 space-y-2" *ngIf="store.cart$ | async as cart">
        <article *ngFor="let item of cart.items" class="flex items-center justify-between rounded border p-3">
          <div>
            <p class="font-medium">{{ item.productName }}</p>
            <p class="text-sm text-slate-500">Qty: {{ item.qty }} x Rs {{ item.price }}</p>
          </div>
          <div class="flex items-center gap-2">
            <span class="font-semibold">Rs {{ item.lineTotal }}</span>
            <button class="rounded bg-rose-100 px-2 py-1 text-rose-700" (click)="remove(item.productId)">Remove</button>
          </div>
        </article>
        <p class="text-right text-lg font-bold">Subtotal: Rs {{ cart.subtotal }}</p>
      </div>
      <div class="mt-4 flex gap-2">
        <a routerLink="/customer/products" class="rounded bg-slate-200 px-3 py-2">Continue Shopping</a>
        <a routerLink="/customer/checkout" class="rounded bg-brand-500 px-3 py-2 text-white">Checkout</a>
      </div>
    </section>
  `
})
export class CartPageComponent implements OnInit {
  constructor(readonly store: CustomerStore) {}

  ngOnInit(): void {
    this.store.loadCart().subscribe();
  }

  remove(productId: number): void {
    this.store.removeCartItem(productId).subscribe();
  }
}
