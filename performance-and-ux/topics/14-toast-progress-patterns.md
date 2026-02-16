# 14) Toast + progress patterns

Billing পেমেন্ট সফল/ত্রুটি দ্রুত জানাতে toast; রিপোর্ট ডাউনলোডে progress bar দেখিয়ে আস্থা বাড়ান।

## Why this matters (real-world)
- ফিডব্যাক ছাড়া ব্যবহারকারী পুনরায় ক্লিক করে দ্বৈত রিকোয়েস্ট পাঠায়।
- Retry বোতাম থাকলে সাপোর্ট টিকিট কমে।
- ইন্টারভিউ: “UX feedback patterns?”

## Concepts
### Beginner
- Success/error toast; auto-dismiss।
- Route-level progress bar (simple). 
### Intermediate
- Retry action; dedupe (same message ছাঁটাই)।
- Upload/download progress with HttpClient reportProgress।
### Advanced
- Global toast service with Subject; keyboard dismiss; accessible live region।

## Copy-paste Example
```ts
// app/shared/ui/toast.service.ts
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
```
```ts
// app/shared/ui/toast-container.component.ts
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from './toast.service';
@Component({
  standalone: true,
  selector: 'hms-toast-container',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: { 'aria-live': 'polite' },
  template: `
    <div class="fixed right-4 top-4 space-y-2">
      <div *ngFor="let t of svc.toasts()" class="px-3 py-2 rounded shadow bg-white border" [class.border-green-500]="t.type==='success'" [class.border-red-500]="t.type==='error'">
        <span>{{ t.message }}</span>
        <button *ngIf="t.action" class="ml-2 text-blue-600" (click)="t.action.onClick()">{{ t.action.label }}</button>
        <button class="ml-2 text-slate-500" (click)="svc.dismiss(t.id)">x</button>
      </div>
    </div>
  `
})
export class ToastContainerComponent { svc = inject(ToastService); }
```
```ts
// app/features/billing/billing-download.component.ts
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpEventType } from '@angular/common/http';
import { ToastService } from '../../shared/ui/toast.service';
@Component({
  standalone: true,
  selector: 'hms-billing-download',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button class="border px-3" (click)="download()">Download invoice CSV</button>
    <div *ngIf="progress > 0 && progress < 100">Progress: {{ progress }}%</div>
  `
})
export class BillingDownloadComponent {
  private http = inject(HttpClient);
  private toast = inject(ToastService);
  progress = 0;
  download() {
    this.progress = 0;
    this.http.get('/api/billing/export', { responseType: 'blob', observe: 'events', reportProgress: true })
      .subscribe({
        next: e => {
          if (e.type === HttpEventType.DownloadProgress && e.total) {
            this.progress = Math.round((e.loaded / e.total) * 100);
          }
          if (e.type === HttpEventType.Response) {
            this.toast.show({ type: 'success', message: 'Downloaded' });
          }
        },
        error: err => this.toast.show({ type: 'error', message: 'Download failed', action: { label: 'Retry', onClick: () => this.download() } })
      });
  }
}
```

## Try it
- Beginner: toast container যোগ করে success/error টেস্ট করুন।
- Advanced: duplicate toast message হলে প্রথমটাই রাখুন (dedupe logic যোগ করুন)।

## Common mistakes
- Live region/aria না দিয়ে toast announce না করা।
- Toast queue পরিষ্কার না করে মেমরি বাড়ানো।

## Interview points
- Toast service + auto-dismiss + retry action।
- Progress with HttpClient reportProgress উল্লেখ।

## Done when…
- Toast service + container কাজ করে।
- Progress indicator প্রদর্শিত।
- Retry action error কেসে আছে।
