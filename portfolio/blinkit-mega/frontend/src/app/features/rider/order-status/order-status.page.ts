import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <div class="mb-3 flex items-center justify-between">
        <h2 class="text-xl font-semibold">Rider Order Status</h2>
        <a routerLink="/rider/batches" class="text-sm text-brand-700"><- Batches</a>
      </div>

      <div class="grid gap-2 md:grid-cols-4">
        <input class="rounded border px-3 py-2" [(ngModel)]="orderId" type="number" placeholder="orderId" />
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="accept()">Accept Order</button>
      </div>

      <div class="mt-3 grid gap-2 md:grid-cols-4">
        <select class="rounded border px-3 py-2" [(ngModel)]="eventType">
          <option value="PICKED_UP">PICKED_UP</option>
          <option value="OUT_FOR_DELIVERY">OUT_FOR_DELIVERY</option>
          <option value="DELIVERED">DELIVERED</option>
        </select>
        <input class="rounded border px-3 py-2 md:col-span-2" [(ngModel)]="messageBody" placeholder="message" />
        <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="update()">Update Status</button>
      </div>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>
    </section>
  `
})
export class RiderOrderStatusPageComponent implements OnInit {
  orderId = 0;
  eventType: 'PICKED_UP' | 'OUT_FOR_DELIVERY' | 'DELIVERED' = 'OUT_FOR_DELIVERY';
  messageBody = 'Rider status update';
  ok = true;
  message = '';

  constructor(private readonly api: ApiService, private readonly route: ActivatedRoute) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.queryParamMap.get('orderId')) || 0;
  }

  accept(): void {
    this.api.riderAccept(this.orderId).subscribe({
      next: () => {
        this.ok = true;
        this.message = 'Order accepted';
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Accept failed';
      }
    });
  }

  update(): void {
    this.api.riderStatus(this.orderId, { eventType: this.eventType, message: this.messageBody }).subscribe({
      next: () => {
        this.ok = true;
        this.message = `Updated ${this.eventType}`;
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Status update failed';
      }
    });
  }
}
