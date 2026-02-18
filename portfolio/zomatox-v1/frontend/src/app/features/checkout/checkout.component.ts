import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';
import { CartStore } from '../../core/cart.store';
import { Address } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Checkout</h1>

  <div class="bg-white border rounded-xl p-4">
    <div class="text-sm text-slate-600 mb-3">Select one of your saved addresses.</div>

    <div class="flex items-center gap-2">
      <select class="border rounded px-3 py-2 min-w-80" [value]="addressId()" (change)="addressId.set(+$any($event.target).value)">
        <option *ngFor="let a of addresses()" [value]="a.id">
          #{{a.id}} • {{a.line1}}, {{a.city}} ({{a.pincode}})
        </option>
      </select>
      <button class="bg-black text-white rounded px-4 py-2" [disabled]="!addressId()" (click)="place()">Place Order</button>
    </div>
    <div class="mt-3 flex items-center gap-2">
      <input class="border rounded px-3 py-2" placeholder="Coupon code" [(ngModel)]="couponCode" />
      <button class="border rounded px-3 py-2" (click)="applyCoupon()">Apply</button>
    </div>
    <div *ngIf="pricing()" class="mt-2 text-sm">
      Item: ₹{{pricing()!.itemTotal}} | Delivery: ₹{{pricing()!.deliveryFee}} |
      Platform: ₹{{pricing()!.platformFee}} | Discount: ₹{{pricing()!.discount}} |
      <b>Payable: ₹{{pricing()!.payableTotal}}</b>
    </div>

    <div *ngIf="!addresses().length" class="mt-3 text-sm text-amber-700">
      No addresses found for current user. Switch user or seed addresses.
    </div>
    <div *ngIf="error()" class="mt-3 text-red-600 text-sm">{{error()}}</div>
  </div>
  `,
})
export class CheckoutComponent {
  private api = inject(ApiService);
  private cart = inject(CartStore);
  private router = inject(Router);

  addresses = signal<Address[]>([]);
  addressId = signal<number>(0);
  error = signal<string>('');
  pricing = signal<any | null>(null);
  couponCode = '';

  constructor() {
    this.api.myAddresses().subscribe({
      next: (addresses) => {
        this.addresses.set(addresses);
        this.addressId.set(addresses.length ? addresses[0].id : 0);
      },
      error: (e) => this.error.set(e?.error?.message ?? 'Failed to load addresses'),
    });
  }

  applyCoupon() {
    this.error.set('');
    const restaurantId = this.cart.cart()?.restaurantId;
    if (!this.couponCode || !restaurantId) {
      this.error.set('Coupon code and cart restaurant are required.');
      return;
    }
    this.api.applyCoupon(this.couponCode, restaurantId).subscribe({
      next: (p) => this.pricing.set(p),
      error: (e) => this.error.set(e?.error?.message ?? 'Failed to apply coupon'),
    });
  }

  place() {
    this.error.set('');
    if (!this.addressId()) {
      this.error.set('Please select a valid address.');
      return;
    }
    this.api.createOrder(this.addressId(), this.couponCode || undefined).subscribe({
      next: (o) => {
        this.cart.load();
        this.router.navigate(['/orders', o.id]);
      },
      error: (e) => this.error.set(e?.error?.message ?? 'Failed'),
    });
  }
}
