import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <div class="mb-3 flex items-center justify-between">
        <h2 class="text-xl font-semibold">My Orders</h2>
        <button class="rounded bg-slate-200 px-3 py-2" (click)="reload()">Refresh</button>
      </div>
      <div class="space-y-2">
        <article *ngFor="let o of store.orders$ | async" class="rounded border p-3">
          <div class="flex justify-between">
            <p class="font-semibold">Order #{{ o.id }}</p>
            <p class="text-sm">{{ o.status }}</p>
          </div>
          <p class="text-sm text-slate-500">{{ o.createdAt }}</p>
          <p class="mt-1 font-semibold">Total Rs {{ o.totalAmount }}</p>
          <div class="mt-2 flex gap-2">
            <a [routerLink]="['/customer/orders', o.id]" class="rounded bg-brand-500 px-3 py-2 text-white">Track</a>
          </div>
        </article>
      </div>
    </section>
  `
})
export class OrdersPageComponent implements OnInit {
  constructor(readonly store: CustomerStore) {}

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.store.loadOrders().subscribe();
  }
}
