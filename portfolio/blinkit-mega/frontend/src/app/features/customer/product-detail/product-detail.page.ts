import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';
import { ProductView } from '../../../core/models/api.models';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <article *ngIf="product" class="rounded-2xl border bg-white p-5">
      <a routerLink="/customer/products" class="text-sm text-brand-700"><- Back to products</a>
      <h2 class="mt-2 text-2xl font-semibold">{{ product.name }}</h2>
      <p class="mt-2 text-slate-600">{{ product.description }}</p>
      <p class="mt-4 text-xl font-bold">Rs {{ product.price }}</p>
      <button class="mt-4 rounded bg-brand-500 px-3 py-2 text-white" (click)="add()">Add to Cart</button>
    </article>
  `
})
export class ProductDetailPageComponent implements OnInit {
  product: ProductView | null = null;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly api: ApiService,
    private readonly store: CustomerStore
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.product(id, this.store.storeId()).subscribe((p) => (this.product = p));
  }

  add(): void {
    if (!this.product) {
      return;
    }
    this.store.addCartItem(this.product.id, 1).subscribe();
  }
}
