import { Injectable, signal } from '@angular/core';

export type Toast = { id: string; type: 'success'|'error'|'info'; message: string; action?: { label: string; onClick: () => void } };

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts = signal<Toast[]>([]);
  show(toast: Omit<Toast, 'id'>) {
    const id = crypto.randomUUID();
    this.toasts.update(list => [...list, { ...toast, id }]);
    setTimeout(() => this.dismiss(id), 4000);
  }
  dismiss(id: string) { this.toasts.update(list => list.filter(t => t.id !== id)); }
}
