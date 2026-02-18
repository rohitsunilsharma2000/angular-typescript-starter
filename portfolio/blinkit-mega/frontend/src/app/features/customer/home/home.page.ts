import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../core/api/api.service';
import { AuthStore, AppRole } from '../../../core/auth/auth.store';
import { CustomerStore } from '../data-access/customer.store';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <section class="grid gap-4 md:grid-cols-2">
      <article class="rounded-2xl border bg-white p-5">
        <h2 class="text-xl font-semibold">Quick Login</h2>
        <p class="text-sm text-slate-500 mt-1">Seed users: user/admin/rider/warehouse</p>
        <div class="mt-4 flex flex-wrap gap-2">
          <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="quickLogin('user@blinkit.com','user123','CUSTOMER')">Login Customer</button>
          <button class="rounded bg-slate-700 px-3 py-2 text-white" (click)="quickLogin('admin@blinkit.com','admin123','ADMIN')">Login Admin</button>
          <button class="rounded bg-emerald-600 px-3 py-2 text-white" (click)="quickLogin('rider@blinkit.com','rider123','RIDER')">Login Rider</button>
          <button class="rounded bg-amber-600 px-3 py-2 text-white" (click)="quickLogin('warehouse@blinkit.com','warehouse123','WAREHOUSE_STAFF')">Login Warehouse</button>
        </div>
        <p class="mt-3 text-sm" [class.text-emerald-700]="ok" [class.text-rose-700]="!ok">{{ message }}</p>
      </article>

      <article class="rounded-2xl border bg-white p-5">
        <h2 class="text-xl font-semibold">Store Resolve</h2>
        <div class="mt-4 flex gap-2">
          <input [(ngModel)]="pincode" class="w-full rounded border px-3 py-2" placeholder="Enter pincode e.g. 700091" />
          <button class="rounded bg-brand-500 px-3 py-2 text-white" (click)="resolveStore()">Resolve</button>
        </div>
        <p class="mt-3 text-sm text-slate-600">Current Store ID: {{ store.storeId() }}</p>
        <div class="mt-5 flex gap-2">
          <a routerLink="/customer/products" class="rounded bg-slate-900 px-3 py-2 text-white">Browse Products</a>
          <a routerLink="/customer/cart" class="rounded bg-slate-200 px-3 py-2">Open Cart</a>
        </div>
      </article>
    </section>
  `
})
export class HomePageComponent {
  pincode = '700091';
  message = 'Not logged in';
  ok = false;

  constructor(
    private readonly api: ApiService,
    private readonly auth: AuthStore,
    readonly store: CustomerStore,
    private readonly router: Router
  ) {}

  quickLogin(email: string, password: string, role: AppRole): void {
    this.api.login({ email, password }).subscribe({
      next: (res) => {
        this.auth.setSession(res.accessToken, role);
        this.ok = true;
        this.message = `${role} login success`;
        this.router.navigateByUrl(role === 'CUSTOMER' ? '/customer/products' : '/');
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Login failed';
      }
    });
  }

  resolveStore(): void {
    this.api.resolveStore(this.pincode).subscribe({
      next: (res) => {
        this.store.setStoreId(res.id);
        this.ok = true;
        this.message = `Resolved store ${res.name} (#${res.id})`;
      },
      error: (err) => {
        this.ok = false;
        this.message = err?.error?.message || 'Store resolve failed';
      }
    });
  }
}
