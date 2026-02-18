import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
  <a routerLink="/delivery/jobs" class="text-sm underline">← Back</a>

  <div *ngIf="order()" class="mt-3 bg-white border rounded-xl p-4">
    <div class="flex justify-between">
      <div class="text-xl font-bold">Order #{{order()!.id}}</div>
      <div class="text-sm bg-slate-100 rounded px-2 py-1">{{order()!.status}}</div>
    </div>

    <div class="mt-3 flex gap-2">
      <button class="border rounded px-3 py-2" (click)="set('PICKED_UP')">PICKED_UP</button>
      <button class="border rounded px-3 py-2" (click)="set('OUT_FOR_DELIVERY')">OUT_FOR_DELIVERY</button>
      <button class="bg-black text-white rounded px-3 py-2" (click)="set('DELIVERED')">DELIVERED</button>
    </div>

    <div *ngIf="msg()" class="mt-3 text-sm">{{msg()}}</div>
  </div>
  `,
})
export class DeliveryOrderComponent {
  private api = inject(ApiService);
  private route = inject(ActivatedRoute);

  order = signal<any | null>(null);
  msg = signal('');

  private id = Number(this.route.snapshot.paramMap.get('id'));

  constructor() {
    this.reload();
  }

  reload() {
    this.api.order(this.id).subscribe(o => this.order.set(o));
  }

  set(next: string) {
    this.msg.set('');
    this.api.deliverySetStatus(this.id, next).subscribe({
      next: () => {
        this.msg.set('Updated to ' + next);
        this.reload();
      },
      error: e => this.msg.set(e?.error?.message ?? 'Failed'),
    });
  }
}
