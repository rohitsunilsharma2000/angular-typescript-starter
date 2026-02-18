import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/api.service';
import { CartStore } from '../../core/cart.store';
import { MenuItem, Restaurant } from '../../core/models';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
  <a routerLink="/restaurants" class="text-sm underline">← Back</a>

  <div *ngIf="restaurant()" class="mt-3 bg-white border rounded-xl overflow-hidden">
    <img class="w-full h-44 object-cover" [src]="restaurant()!.imageUrl" />
    <div class="p-4">
      <div class="text-xl font-bold">{{restaurant()!.name}}</div>
      <div class="text-sm text-slate-600">{{restaurant()!.city}} • {{restaurant()!.cuisineType}}</div>
      <div class="mt-1 text-sm">⭐ {{restaurant()!.ratingAvg.toFixed(1)}} • {{restaurant()!.deliveryTimeMin}} min</div>
    </div>
  </div>

  <h2 class="mt-6 mb-2 font-semibold">Menu</h2>

  <div class="grid gap-3">
    <div *ngFor="let m of menu()"
         class="bg-white border rounded-xl p-4 flex items-center gap-4">
      <div class="flex-1">
        <div class="font-medium">{{m.name}}</div>
        <div class="text-sm text-slate-600">₹{{m.price}} • {{m.isVeg ? 'Veg' : 'Non-veg'}} • Stock: {{m.stockQty}}</div>
        <div class="text-xs" [class.text-red-600]="!m.available || m.stockQty<=0">
          {{(!m.available || m.stockQty<=0) ? 'Unavailable' : 'Available'}}
        </div>
      </div>

      <div class="flex items-center gap-2">
        <input class="w-16 border rounded px-2 py-1" type="number" min="1" [(ngModel)]="qty[m.id]" />
        <button class="bg-black text-white rounded px-3 py-2"
                [disabled]="!m.available || m.stockQty<=0"
                (click)="add(m.id)">
          Add
        </button>
      </div>
    </div>
  </div>
  `,
})
export class RestaurantDetailComponent {
  private api = inject(ApiService);
  private cart = inject(CartStore);
  private route = inject(ActivatedRoute);

  restaurant = signal<Restaurant | null>(null);
  menu = signal<MenuItem[]>([]);
  qty: Record<number, number> = {};

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.api.restaurant(id).subscribe(r => this.restaurant.set(r));
    this.api.menu(id).subscribe(ms => {
      this.menu.set(ms);
      ms.forEach(m => this.qty[m.id] = 1);
    });
  }

  add(menuItemId: number) {
    const q = this.qty[menuItemId] ?? 1;
    this.cart.upsert(menuItemId, q);
  }
}
