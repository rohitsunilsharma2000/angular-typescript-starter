import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Admin Inventory Update</h2>
      <div class="mt-4 grid gap-2 md:grid-cols-4">
        <input class="rounded border px-3 py-2" [(ngModel)]="storeId" type="number" placeholder="storeId" />
        <input class="rounded border px-3 py-2" [(ngModel)]="productId" type="number" placeholder="productId" />
        <input class="rounded border px-3 py-2" [(ngModel)]="stockOnHand" type="number" placeholder="stock" />
        <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="update()">Update</button>
      </div>
      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>
      <p class="mt-2 text-xs text-slate-500">Tip: seeded storeId=1, productId=1..80</p>
    </section>
  `
})
export class AdminInventoryPageComponent {
  storeId = 1;
  productId = 1;
  stockOnHand = 50;
  ok = true;
  message = '';

  constructor(private readonly api: ApiService) {}

  update(): void {
    this.api.adminUpdateInventory(this.storeId, this.productId, this.stockOnHand).subscribe({
      next: () => {
        this.ok = true;
        this.message = 'Inventory updated';
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Inventory update failed';
      }
    });
  }
}
