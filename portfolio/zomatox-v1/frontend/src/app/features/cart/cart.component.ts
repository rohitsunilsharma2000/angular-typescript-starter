import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartStore } from '../../core/cart.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Cart</h1>

  <div *ngIf="cartStore.cart() as cart" class="bg-white border rounded-xl p-4">
    <div *ngIf="cart.items.length===0" class="text-slate-600">
      Cart is empty. <a routerLink="/restaurants" class="underline">Browse restaurants</a>
    </div>

    <div *ngFor="let it of cart.items" class="flex items-center justify-between py-2 border-b last:border-b-0">
      <div>
        <div class="font-medium">{{it.name}}</div>
        <div class="text-sm text-slate-600">₹{{it.price}} × {{it.qty}} = ₹{{it.lineTotal}}</div>
      </div>

      <div class="flex gap-2">
        <button class="border rounded px-2 py-1" (click)="cartStore.upsert(it.menuItemId, it.qty-1)" [disabled]="it.qty<=1">-</button>
        <button class="border rounded px-2 py-1" (click)="cartStore.upsert(it.menuItemId, it.qty+1)">+</button>
        <button class="border rounded px-2 py-1" (click)="cartStore.remove(it.menuItemId)">Remove</button>
      </div>
    </div>

    <div class="mt-4 flex items-center justify-between">
      <div class="font-semibold">Item Total: ₹{{cart.itemTotal}}</div>
      <a routerLink="/checkout" class="bg-black text-white rounded px-4 py-2" [class.opacity-50]="cart.items.length===0"
         [attr.aria-disabled]="cart.items.length===0">Checkout</a>
    </div>
  </div>
  `,
})
export class CartComponent {
  cartStore = inject(CartStore);

  constructor() {
    this.cartStore.load();
  }
}
