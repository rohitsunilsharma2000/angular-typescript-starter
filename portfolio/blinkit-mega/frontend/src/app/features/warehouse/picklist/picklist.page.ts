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
        <h2 class="text-xl font-semibold">Warehouse Picklist</h2>
        <a routerLink="/warehouse/picking-queue" class="text-sm text-brand-700"><- Queue</a>
      </div>

      <div class="grid gap-2 md:grid-cols-4">
        <input class="rounded border px-3 py-2" [(ngModel)]="orderId" type="number" placeholder="orderId" />
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Load Picklist</button>
      </div>

      <div class="mt-3 max-h-72 overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0"><tr><th class="p-2 text-left">OrderItem ID</th><th class="p-2 text-left">Product</th><th class="p-2 text-left">Qty</th><th class="p-2 text-left">Refunded</th></tr></thead>
          <tbody>
            <tr *ngFor="let i of items" class="border-t">
              <td class="p-2">{{ i.id }}</td>
              <td class="p-2">{{ i.productName }}</td>
              <td class="p-2">{{ i.qty }}</td>
              <td class="p-2">{{ i.refundedQty }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="mt-4 grid gap-2 md:grid-cols-4">
        <input class="rounded border px-3 py-2" [(ngModel)]="orderItemId" type="number" placeholder="orderItemId" />
        <input class="rounded border px-3 py-2" [(ngModel)]="missingQty" type="number" placeholder="missingQty" />
        <button class="rounded bg-amber-600 px-3 py-2 text-white" (click)="markMissing()">Mark Missing</button>
        <button class="rounded bg-emerald-600 px-3 py-2 text-white" (click)="packed()">Mark Packed</button>
      </div>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>
    </section>
  `
})
export class WarehousePicklistPageComponent implements OnInit {
  orderId = 0;
  orderItemId = 0;
  missingQty = 1;
  items: any[] = [];
  ok = true;
  message = '';

  constructor(private readonly api: ApiService, private readonly route: ActivatedRoute) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.queryParamMap.get('orderId')) || 0;
    if (this.orderId) {
      this.load();
    }
  }

  load(): void {
    if (!this.orderId) {
      return;
    }
    this.api.warehousePicklist(this.orderId).subscribe({
      next: (res) => {
        this.items = res;
        if (res.length) {
          this.orderItemId = res[0].id;
        }
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Load failed';
      }
    });
  }

  markMissing(): void {
    this.api.warehouseMarkMissing(this.orderId, this.orderItemId, this.missingQty).subscribe({
      next: () => {
        this.ok = true;
        this.message = 'Missing item marked';
        this.load();
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Mark missing failed';
      }
    });
  }

  packed(): void {
    this.api.markPacked(this.orderId).subscribe({
      next: () => {
        this.ok = true;
        this.message = 'Order marked packed';
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Packed failed';
      }
    });
  }
}
