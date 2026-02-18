import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Admin Reviews</h1>
  <div class="flex gap-2 mb-3">
    <button class="border rounded px-2 py-1" (click)="load('VISIBLE')">Visible</button>
    <button class="border rounded px-2 py-1" (click)="load('HIDDEN')">Hidden</button>
  </div>
  <div class="grid gap-2">
    <div *ngFor="let r of rows()" class="border rounded p-3">
      <div>#{{r.id}} ⭐{{r.rating}} {{r.comment}} ({{r.status}})</div>
      <div class="flex gap-2 mt-2">
        <button class="border rounded px-2" (click)="hide(r.id)">Hide</button>
        <button class="border rounded px-2" (click)="unhide(r.id)">Unhide</button>
      </div>
    </div>
  </div>
  `,
})
export class AdminReviewsComponent {
  private api = inject(ApiService);
  rows = signal<any[]>([]);
  status = 'VISIBLE';

  constructor() { this.load(this.status); }

  load(status: string) { this.status = status; this.api.adminReviews(status).subscribe(v => this.rows.set(v)); }
  hide(id: number) { this.api.adminHideReview(id).subscribe(() => this.load(this.status)); }
  unhide(id: number) { this.api.adminUnhideReview(id).subscribe(() => this.load(this.status)); }
}
