import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';
import { CheckoutPreviewResponse } from '../../../core/models/api.models';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Checkout</h2>
      <div class="mt-4 flex gap-2">
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="preview()">Checkout Preview</button>
        <button class="rounded bg-brand-500 px-3 py-2 text-white" [disabled]="!previewData" (click)="placeOrder()">Place Order</button>
      </div>

      <article *ngIf="previewData" class="mt-4 rounded border p-4">
        <p>Reservation ID: <b>{{ previewData.reservationId }}</b></p>
        <p>Expires At: {{ previewData.expiresAt }}</p>
        <p>Subtotal: Rs {{ previewData.subtotal }}</p>
        <p>Delivery Fee: Rs {{ previewData.deliveryFee }}</p>
        <p class="font-bold">Total: Rs {{ previewData.total }}</p>
      </article>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>
    </section>
  `
})
export class CheckoutPageComponent {
  previewData: CheckoutPreviewResponse | null = null;
  message = '';
  ok = true;

  constructor(
    private readonly api: ApiService,
    private readonly store: CustomerStore,
    private readonly router: Router
  ) {}

  preview(): void {
    this.api.checkoutPreview(this.store.storeId()).subscribe({
      next: (res) => {
        this.previewData = res;
        this.ok = true;
        this.message = 'Reservation successful';
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Preview failed';
      }
    });
  }

  placeOrder(): void {
    if (!this.previewData?.reservationId) {
      return;
    }
    const key = `idem-${Date.now()}`;
    this.api.placeOrder(this.previewData.reservationId, null, 0, key).subscribe({
      next: (order) => {
        this.ok = true;
        this.message = `Order #${order.id} created`;
        this.router.navigateByUrl(`/customer/orders/${order.id}`);
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Place order failed';
      }
    });
  }
}
