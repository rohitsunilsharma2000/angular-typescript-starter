import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../core/api/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="rounded-2xl border bg-white p-5">
      <div class="mb-3 flex items-center justify-between">
        <h2 class="text-xl font-semibold">Admin Audit Logs</h2>
        <button class="rounded bg-slate-900 px-3 py-2 text-white" (click)="load()">Refresh</button>
      </div>
      <div class="max-h-[28rem] overflow-auto rounded border">
        <table class="w-full text-sm">
          <thead class="bg-slate-100 sticky top-0">
            <tr><th class="p-2 text-left">ID</th><th class="p-2 text-left">Action</th><th class="p-2 text-left">Entity</th><th class="p-2 text-left">Entity ID</th><th class="p-2 text-left">At</th></tr>
          </thead>
          <tbody>
            <tr *ngFor="let a of audits" class="border-t">
              <td class="p-2">{{ a.id }}</td>
              <td class="p-2">{{ a.action }}</td>
              <td class="p-2">{{ a.entityType }}</td>
              <td class="p-2">{{ a.entityId }}</td>
              <td class="p-2">{{ a.createdAt }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  `
})
export class AdminAuditPageComponent implements OnInit {
  audits: any[] = [];

  constructor(private readonly api: ApiService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.adminAudit().subscribe({
      next: (res) => (this.audits = res),
      error: () => (this.audits = [])
    });
  }
}
