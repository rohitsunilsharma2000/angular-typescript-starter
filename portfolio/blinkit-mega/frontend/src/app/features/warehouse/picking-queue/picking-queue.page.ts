import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <div class="mb-3 flex items-center justify-between">
        <h2 class="text-xl font-semibold">Warehouse Picking Queue</h2>
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Refresh</button>
      </div>

      <div class="max-h-[28rem] overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0">
            <tr><th class="p-2 text-left">Order ID</th><th class="p-2 text-left">Status</th><th class="p-2 text-left">Store</th><th class="p-2 text-left">Action</th></tr>
          </thead>
          <tbody>
            <tr *ngFor="let o of orders" class="border-t">
              <td class="p-2">{{ o.id }}</td>
              <td class="p-2">{{ o.status }}</td>
              <td class="p-2">{{ o.storeId }}</td>
              <td class="p-2">
                <button class="rounded bg-brand-500 px-2 py-1 text-white" (click)="openPicklist(o.id)">Open Picklist</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <p class="mt-3 text-sm text-slate-500">Use picklist screen to mark missing or mark packed.</p>
    </section>
  `
})
export class WarehousePickingQueuePageComponent implements OnInit {
  orders: any[] = [];

  constructor(private readonly api: ApiService, private readonly router: Router) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.warehousePickingQueue().subscribe({
      next: (res) => (this.orders = res),
      error: () => (this.orders = [])
    });
  }

  openPicklist(orderId: number): void {
    this.router.navigate(['/warehouse/picklist'], { queryParams: { orderId } });
  }
}
