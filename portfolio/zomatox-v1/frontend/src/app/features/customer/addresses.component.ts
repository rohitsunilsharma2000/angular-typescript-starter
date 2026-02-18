import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <h1 class="text-xl font-bold mb-3">Address Book</h1>
  <div class="bg-white border rounded p-3 mb-3 flex gap-2">
    <input class="border rounded px-2 py-1" [(ngModel)]="line1" placeholder="line1" />
    <input class="border rounded px-2 py-1" [(ngModel)]="city" placeholder="city" />
    <input class="border rounded px-2 py-1" [(ngModel)]="pincode" placeholder="pincode" />
    <input class="border rounded px-2 py-1" [(ngModel)]="phone" placeholder="phone" />
    <button class="bg-black text-white rounded px-3" (click)="create()">Add</button>
  </div>
  <div class="grid gap-2">
    <div *ngFor="let a of addresses()" class="border rounded p-3 flex justify-between">
      <div>#{{a.id}} {{a.line1}}, {{a.city}} {{a.pincode}} {{a.phone}}</div>
      <button class="text-red-600" (click)="remove(a.id)">Delete</button>
    </div>
  </div>
  `,
})
export class AddressesComponent {
  private api = inject(ApiService);
  addresses = signal<any[]>([]);
  line1 = ''; city = ''; pincode = ''; phone = '';

  constructor() { this.load(); }

  load() { this.api.myAddresses().subscribe(a => this.addresses.set(a)); }
  create() {
    this.api.createAddress({ line1: this.line1, city: this.city, pincode: this.pincode, phone: this.phone })
      .subscribe(() => { this.line1 = ''; this.city = ''; this.pincode = ''; this.phone = ''; this.load(); });
  }
  remove(id: number) { this.api.deleteAddress(id).subscribe(() => this.load()); }
}
