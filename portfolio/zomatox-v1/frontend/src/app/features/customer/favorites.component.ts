import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Favorites</h1>
  <div class="grid gap-2">
    <div *ngFor="let r of favorites()" class="border rounded p-3 flex justify-between">
      <a [routerLink]="['/restaurants', r.id]">{{r.name}} ({{r.city}})</a>
      <button class="text-red-600" (click)="remove(r.id)">Remove</button>
    </div>
  </div>
  `,
})
export class FavoritesComponent {
  private api = inject(ApiService);
  favorites = signal<any[]>([]);

  constructor() { this.load(); }

  load() { this.api.favorites().subscribe(v => this.favorites.set(v)); }
  remove(id: number) { this.api.removeFavorite(id).subscribe(() => this.load()); }
}
