import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <h1 class="text-xl font-bold mb-3">Delivery • Jobs</h1>

  <div class="flex gap-2 mb-3">
    <button class="border rounded px-3 py-1" (click)="load('AVAILABLE')">AVAILABLE</button>
    <button class="border rounded px-3 py-1" (click)="load('ASSIGNED')">ASSIGNED</button>
  </div>

  <div class="grid gap-3">
    <div *ngFor="let o of jobs()" class="bg-white border rounded-xl p-4">
      <div class="flex justify-between">
        <div class="font-semibold">Order #{{o.id}}</div>
        <div class="text-sm bg-slate-100 rounded px-2 py-1">{{o.status}}</div>
      </div>

      <div class="mt-3 flex gap-2">
        <button *ngIf="mode()==='AVAILABLE'" class="bg-black text-white rounded px-3 py-2" (click)="accept(o.id)">Accept</button>
        <a class="border rounded px-3 py-2" [routerLink]="['/delivery/order', o.id]">Open</a>
      </div>
    </div>
  </div>
  `,
})
export class DeliveryJobsComponent {
  private api = inject(ApiService);
  jobs = signal<any[]>([]);
  mode = signal<'AVAILABLE' | 'ASSIGNED'>('AVAILABLE');

  constructor() {
    this.load('AVAILABLE');
  }

  load(mode: 'AVAILABLE' | 'ASSIGNED') {
    this.mode.set(mode);
    this.api.deliveryJobs(mode).subscribe(j => this.jobs.set(j));
  }

  accept(orderId: number) {
    this.api.deliveryAccept(orderId).subscribe(() => this.load('ASSIGNED'));
  }
}
