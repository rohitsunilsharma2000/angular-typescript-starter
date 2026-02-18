import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <div class="mb-3 flex items-center justify-between">
        <h2 class="text-xl font-semibold">Rider Active Batches</h2>
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Refresh</button>
      </div>

      <div class="space-y-3">
        <article *ngFor="let b of batches" class="rounded border p-3">
          <p class="font-semibold">Batch #{{ b.id }} ({{ b.status }})</p>
          <p class="text-sm text-slate-500">Created: {{ b.createdAt }}</p>
        </article>
      </div>

      <div class="mt-4 flex gap-2">
        <button class="rounded bg-slate-200 px-3 py-2" (click)="loadSeedAndGo()">Go to Seeded Order Status</button>
      </div>
    </section>
  `
})
export class RiderBatchesPageComponent implements OnInit {
  batches: any[] = [];

  constructor(private readonly api: ApiService, private readonly router: Router) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.riderBatches('ACTIVE').subscribe({
      next: (res) => (this.batches = res),
      error: () => (this.batches = [])
    });
  }

  loadSeedAndGo(): void {
    this.api.seedMap().subscribe((seed) => {
      const orderId = seed?.seededIds?.pickingOrderId;
      if (orderId) {
        this.router.navigate(['/rider/order-status'], { queryParams: { orderId } });
      }
    });
  }
}
