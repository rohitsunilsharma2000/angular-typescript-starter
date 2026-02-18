import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="max-w-md mx-auto bg-white border rounded-xl p-6 mt-10">
    <h1 class="text-2xl font-bold mb-4">Login</h1>

    <div class="space-y-3">
      <input class="w-full border rounded px-3 py-2" [(ngModel)]="email" placeholder="email" />
      <input class="w-full border rounded px-3 py-2" [(ngModel)]="password" placeholder="password" type="password" />
      <button class="w-full bg-black text-white rounded px-4 py-2" (click)="login()">Login</button>
    </div>

    <div class="text-xs text-slate-600 mt-4">
      Seed users: customer&#64;zomatox.local / customer123, owner&#64;zomatox.local / owner123,
      delivery&#64;zomatox.local / delivery123, admin&#64;zomatox.local / admin123
    </div>

    <div *ngIf="error()" class="mt-3 text-sm text-red-600">{{error()}}</div>
  </div>
  `,
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  email = 'customer@zomatox.local';
  password = 'customer123';
  error = signal('');

  login() {
    this.error.set('');
    this.auth.login(this.email, this.password).subscribe({
      next: (res) => {
        const role = res.user.role;
        if (role === 'OWNER') this.router.navigate(['/owner/orders']);
        else if (role === 'DELIVERY_PARTNER') this.router.navigate(['/delivery/jobs']);
        else if (role === 'ADMIN') this.router.navigate(['/admin/restaurants']);
        else this.router.navigate(['/restaurants']);
      },
      error: (e) => this.error.set(e?.error?.message ?? 'Login failed'),
    });
  }
}
