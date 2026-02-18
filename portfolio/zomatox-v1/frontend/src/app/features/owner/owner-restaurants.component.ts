import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Owner • My Restaurants</h1>
  <div class="text-sm text-slate-600 mb-3">Uses X-User-Role=OWNER</div>

  <div class="grid gap-3">
    <div *ngFor="let r of restaurants()" class="bg-white border rounded-xl p-4">
      <div class="font-semibold">{{r.name}}</div>
      <div class="text-sm text-slate-600">{{r.city}} • {{r.cuisineType}}</div>
    </div>
  </div>
  `,
})
export class OwnerRestaurantsComponent {
  private api = inject(ApiService);
  restaurants = signal<any[]>([]);

  constructor() {
    this.api.ownerRestaurants().subscribe(rs => this.restaurants.set(rs));
  }
}
