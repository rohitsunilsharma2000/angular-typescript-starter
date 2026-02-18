import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Admin Orders</h2>

      <div class="mt-4 grid gap-2 md:grid-cols-4">
        <input class="rounded border px-3 py-2" [(ngModel)]="statusFilter" placeholder="status e.g. PICKING" />
        <input class="rounded border px-3 py-2" [(ngModel)]="storeIdFilter" type="number" placeholder="storeId" />
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Filter</button>
        <button class="rounded bg-slate-200 px-3 py-2" (click)="loadSeedMap()">Load Seed IDs</button>
      </div>

      <div class="mt-3 grid gap-2 md:grid-cols-3">
        <input class="rounded border px-3 py-2" [(ngModel)]="actionOrderId" type="number" placeholder="orderId" />
        <input class="rounded border px-3 py-2" [(ngModel)]="riderUserId" type="number" placeholder="riderUserId" />
        <button class="rounded bg-emerald-600 px-3 py-2 text-white" (click)="assignRider()">Assign Rider</button>
      </div>

      <div class="mt-2 grid gap-2 md:grid-cols-3">
        <input class="rounded border px-3 py-2" [(ngModel)]="nextStatus" placeholder="nextStatus e.g. CONFIRMED" />
        <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="advance()">Advance Status</button>
      </div>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>

      <div class="mt-3 max-h-96 overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0">
            <tr><th class="p-2 text-left">ID</th><th class="p-2 text-left">Status</th><th class="p-2 text-left">Store</th><th class="p-2 text-left">User</th><th class="p-2 text-left">Total</th></tr>
          </thead>
          <tbody>
            <tr *ngFor="let o of orders" class="border-t">
              <td class="p-2">{{ o.id }}</td><td class="p-2">{{ o.status }}</td><td class="p-2">{{ o.storeId }}</td><td class="p-2">{{ o.userId }}</td><td class="p-2">{{ o.totalAmount }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  `
})
export class AdminOrdersPageComponent implements OnInit {
  statusFilter = '';
  storeIdFilter: number | undefined;
  actionOrderId = 0;
  riderUserId = 3;
  nextStatus = 'CONFIRMED';
  orders: any[] = [];
  ok = true;
  message = '';

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.adminOrders(this.statusFilter, this.storeIdFilter).subscribe({
      next: (res) => (this.orders = res),
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Failed to load orders';
      }
    });
  }

  loadSeedMap(): void {
    this.api.seedMap().subscribe((seed) => {
      const ids = seed?.seededIds || {};
      this.actionOrderId = ids.pickingOrderId || this.actionOrderId;
      this.riderUserId = ids.riderUserId || this.riderUserId;
    });
  }

  assignRider(): void {
    this.api.adminAssignRider(this.actionOrderId, this.riderUserId).subscribe({
      next: () => {
        this.ok = true;
        this.message = 'Rider assigned';
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Assign failed';
      }
    });
  }

  advance(): void {
    this.api.adminAdvanceStatus(this.actionOrderId, this.nextStatus).subscribe({
      next: () => {
        this.ok = true;
        this.message = `Order moved to ${this.nextStatus}`;
        this.load();
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Advance failed';
      }
    });
  }
}
