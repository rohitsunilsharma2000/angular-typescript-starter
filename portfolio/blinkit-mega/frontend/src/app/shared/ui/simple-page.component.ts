import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-simple-page',
  template: `
    <section class="rounded-2xl p-8 bg-white shadow-sm border border-slate-200">
      <h1 class="text-2xl font-bold text-slate-800">{{ title }}</h1>
      <p class="mt-2 text-slate-600">API wiring ready. Replace this shell with feature implementation.</p>
    </section>
  `
})
export class SimplePageComponent {
  title = this.route.snapshot.data['title'] ?? 'Page';
  constructor(private readonly route: ActivatedRoute) {}
}
