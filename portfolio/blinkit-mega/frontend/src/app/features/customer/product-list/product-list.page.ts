import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <section>
      <div class="mb-4 flex gap-2">
        <input [(ngModel)]="q" placeholder="Search products" class="w-full rounded border px-3 py-2" />
        <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="load()">Search</button>
        <a routerLink="/customer/cart" class="rounded bg-slate-900 px-3 py-2 text-white">Cart</a>
      </div>

      <div class="grid gap-3 md:grid-cols-3">
        <article *ngFor="let p of store.products$ | async" class="rounded-xl border bg-white p-4">
          <h3 class="font-semibold">{{ p.name }}</h3>
          <p class="text-sm text-slate-500 mt-1">{{ p.description }}</p>
          <p class="mt-3 font-bold">Rs {{ p.price }}</p>
          <div class="mt-3 flex gap-2">
            <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="add(p.id)">Add to Cart</button>
            <a [routerLink]="['/customer/products', p.id]" class="rounded bg-slate-200 px-3 py-2">Detail</a>
          </div>
        </article>
      </div>
    </section>
  `
})
export class ProductListPageComponent implements OnInit {
  q = '';

  constructor(readonly store: CustomerStore) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.store.loadProducts(this.q).subscribe();
  }

  add(productId: number): void {
    this.store.addCartItem(productId, 1).subscribe();
  }
}
