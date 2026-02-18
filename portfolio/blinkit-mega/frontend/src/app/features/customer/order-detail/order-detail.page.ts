import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import { ApiService } from '../../../core/api/api.service';
import { OrderView } from '../../../core/models/api.models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="rounded-2xl border bg-white p-5" *ngIf="order">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-semibold">Order #{{ order.id }}</h2>
        <a routerLink="/customer/orders" class="text-sm text-brand-700"><- Back</a>
      </div>

      <p class="mt-2">Status: <span class="font-semibold">{{ order.status }}</span></p>
      <p class="text-sm text-slate-500">Created: {{ order.createdAt }}</p>

      <div class="mt-4 space-y-2">
        <article *ngFor="let i of order.items" class="rounded border p-2">
          {{ i.productName }} - Qty {{ i.qty }} - Rs {{ i.price }}
        </article>
      </div>

      <div class="mt-4 flex gap-2">
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="startPayment()">Start Payment</button>
        <button class="rounded bg-emerald-600 px-3 py-2 text-white" (click)="confirmPayment('SUCCESS')">Confirm Success</button>
        <button class="rounded bg-rose-600 px-3 py-2 text-white" (click)="confirmPayment('FAIL')">Confirm Fail</button>
      </div>

      <div class="mt-5">
        <h3 class="font-semibold">Live Timeline</h3>
        <div class="mt-2 rounded border p-3 text-sm bg-slate-50 max-h-52 overflow-auto">
          <p *ngFor="let t of timeline">{{ t }}</p>
        </div>
      </div>
    </section>
  `
})
export class OrderDetailPageComponent implements OnInit, OnDestroy {
  orderId = 0;
  order: OrderView | null = null;
  timeline: string[] = [];
  private pollSub?: Subscription;
  private sse?: EventSource;

  constructor(private readonly route: ActivatedRoute, private readonly api: ApiService) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    this.load();

    // SSE may fail if server rejects unauthenticated EventSource headers, so keep polling fallback.
    this.sse = this.api.orderStream(this.orderId);
    this.sse.onmessage = (evt) => this.timeline.unshift(`[SSE] ${evt.data}`);
    this.sse.onerror = () => this.timeline.unshift('[SSE] Stream disconnected, polling continues');

    this.pollSub = interval(5000).subscribe(() => this.load());
  }

  ngOnDestroy(): void {
    this.pollSub?.unsubscribe();
    this.sse?.close();
  }

  load(): void {
    this.api.orderById(this.orderId).subscribe((res) => {
      this.order = res;
      this.timeline.unshift(`[POLL] Status ${res.status}`);
      this.timeline = this.timeline.slice(0, 20);
    });
  }

  startPayment(): void {
    this.api.startPayment(this.orderId).subscribe(() => this.load());
  }

  confirmPayment(result: 'SUCCESS' | 'FAIL'): void {
    this.api.confirmPayment(this.orderId, result).subscribe(() => this.load());
  }
}
