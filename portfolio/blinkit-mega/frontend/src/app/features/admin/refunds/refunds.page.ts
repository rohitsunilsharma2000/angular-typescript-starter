import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Admin Refund Approvals</h2>
      <div class="mt-4 flex gap-2">
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Load Pending</button>
        <button class="rounded bg-slate-200 px-3 py-2" (click)="loadSeedMap()">Use Seed Refund ID</button>
      </div>

      <div class="mt-3 flex gap-2">
        <input class="w-full rounded border px-3 py-2" [(ngModel)]="refundId" type="number" placeholder="refundId" />
        <button class="rounded bg-emerald-600 px-3 py-2 text-white" (click)="approve()">Approve</button>
      </div>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>

      <div class="mt-3 max-h-96 overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0"><tr><th class="p-2 text-left">Refund ID</th><th class="p-2 text-left">Order ID</th><th class="p-2 text-left">Amount</th><th class="p-2 text-left">Status</th></tr></thead>
          <tbody>
            <tr *ngFor="let r of refunds" class="border-t"><td class="p-2">{{ r.id }}</td><td class="p-2">{{ r.orderId }}</td><td class="p-2">{{ r.amount }}</td><td class="p-2">{{ r.status }}</td></tr>
          </tbody>
        </table>
      </div>
    </section>
  `
})
export class AdminRefundsPageComponent implements OnInit {
  refunds: any[] = [];
  refundId = 0;
  ok = true;
  message = '';

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.adminRefunds('PENDING').subscribe({
      next: (res) => (this.refunds = res),
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Failed to load refunds';
      }
    });
  }

  loadSeedMap(): void {
    this.api.seedMap().subscribe((seed) => {
      this.refundId = seed?.seededIds?.refundId || this.refundId;
    });
  }

  approve(): void {
    this.api.adminApproveRefund(this.refundId).subscribe({
      next: () => {
        this.ok = true;
        this.message = `Refund ${this.refundId} approved`;
        this.load();
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Approve failed';
      }
    });
  }
}
