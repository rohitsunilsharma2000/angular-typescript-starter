import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Admin Restaurants</h1>
  <div class="flex gap-2 mb-3">
    <button class="border rounded px-2 py-1" (click)="load('PENDING_APPROVAL')">Pending</button>
    <button class="border rounded px-2 py-1" (click)="load('APPROVED')">Approved</button>
    <button class="border rounded px-2 py-1" (click)="load('REJECTED')">Rejected</button>
  </div>
  <div class="grid gap-2">
    <div *ngFor="let r of rows()" class="border rounded p-3">
      <div class="font-semibold">{{r.name}} - {{r.approvalStatus}}</div>
      <div class="flex gap-2 mt-2">
        <button class="border rounded px-2" (click)="approve(r.id)">Approve</button>
        <button class="border rounded px-2" (click)="reject(r.id)">Reject</button>
      </div>
    </div>
  </div>
  `,
})
export class AdminRestaurantsComponent {
  private api = inject(ApiService);
  rows = signal<any[]>([]);
  status = 'PENDING_APPROVAL';

  constructor() { this.load(this.status); }

  load(status: string) { this.status = status; this.api.adminRestaurants(status).subscribe(v => this.rows.set(v)); }
  approve(id: number) { this.api.adminApproveRestaurant(id).subscribe(() => this.load(this.status)); }
  reject(id: number) { this.api.adminRejectRestaurant(id).subscribe(() => this.load(this.status)); }
}
