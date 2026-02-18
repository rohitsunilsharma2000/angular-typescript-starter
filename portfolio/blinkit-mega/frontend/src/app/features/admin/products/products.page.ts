import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../core/api/api.service';
import { ProductView } from '../../../core/models/api.models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <h2 class="text-xl font-semibold">Admin Products</h2>
      <div class="mt-4 grid gap-2 md:grid-cols-6">
        <input class="rounded border px-3 py-2" [(ngModel)]="form.categoryId" placeholder="categoryId" type="number" />
        <input class="rounded border px-3 py-2" [(ngModel)]="form.name" placeholder="name" />
        <input class="rounded border px-3 py-2" [(ngModel)]="form.description" placeholder="description" />
        <input class="rounded border px-3 py-2" [(ngModel)]="form.price" placeholder="price" type="number" />
        <input class="rounded border px-3 py-2" [(ngModel)]="editId" placeholder="editId(optional)" type="number" />
        <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="save()">Create/Update</button>
      </div>

      <div class="mt-4 flex gap-2">
        <input class="w-full rounded border px-3 py-2" [(ngModel)]="q" placeholder="search" />
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Load</button>
      </div>

      <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>

      <div class="mt-3 max-h-96 overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0"><tr><th class="p-2 text-left">ID</th><th class="p-2 text-left">Name</th><th class="p-2 text-left">Price</th></tr></thead>
          <tbody>
            <tr *ngFor="let p of products" class="border-t"><td class="p-2">{{ p.id }}</td><td class="p-2">{{ p.name }}</td><td class="p-2">{{ p.price }}</td></tr>
          </tbody>
        </table>
      </div>
    </section>
  `
})
export class AdminProductsPageComponent implements OnInit {
  q = '';
  editId: number | null = null;
  ok = true;
  message = '';
  products: ProductView[] = [];
  form = { categoryId: 1, name: 'Demo Product', description: 'admin created', price: 99, active: true };

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.products(1, this.q).subscribe({
      next: (res) => (this.products = res),
      error: () => (this.products = [])
    });
  }

  save(): void {
    const req = this.editId
      ? this.api.adminUpdateProduct(this.editId, this.form)
      : this.api.adminCreateProduct(this.form);

    req.subscribe({
      next: () => {
        this.ok = true;
        this.message = this.editId ? 'Product updated' : 'Product created';
        this.load();
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Save failed';
      }
    });
  }
}
