# 05) Mocking HttpClient properly

Patients API লোডে loading → success → error UI টেস্ট করা দরকার। HttpTestingController দিয়ে response কন্ট্রোল করুন।

## Why this matters (real-world)
- নেটওয়ার্ক ফ্লেকি; deterministic টেস্ট দরকার।
- API error mapping ঠিক আছে কিনা নিশ্চিত করা যায়।

## Concepts
### Beginner
- HttpClientTestingModule, HttpTestingController basics।
- GET success assertion।
### Intermediate
- error response simulate; loading state assert।
- retry/backoff না টেস্ট করলে double-call বাগ।
### Advanced
- Multiple calls order verify; query params match; delay simulation।

## Copy-paste Example
```ts
// patients.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
export type Patient = { id: string; name: string; ward: string };
@Injectable({ providedIn: 'root' })
export class PatientsService {
  constructor(private http: HttpClient) {}
  list() { return this.http.get<Patient[]>('/api/patients'); }
}
```
```ts
// patients.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PatientsService } from './patients.service';

describe('PatientsService', () => {
  let httpMock: HttpTestingController;
  let svc: PatientsService;
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    svc = TestBed.inject(PatientsService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  afterEach(() => httpMock.verify());

  it('returns data on success', () => {
    const mock = [{ id: 'P1', name: 'Rima', ward: 'ICU' }];
    svc.list().subscribe(data => expect(data).toEqual(mock));
    const req = httpMock.expectOne('/api/patients');
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('handles error', () => {
    let errorMsg = '';
    svc.list().subscribe({ error: (err) => errorMsg = err.message });
    const req = httpMock.expectOne('/api/patients');
    req.flush({ message: 'fail' }, { status: 500, statusText: 'Server Error' });
    expect(errorMsg).toContain('500');
  });
});
```
```ts
// patient-list.component.spec.ts (loading → success → error)
import { render, screen } from '@testing-library/angular';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { PatientsService, Patient } from './patients.service';

@Component({
  standalone: true,
  selector: 'hms-patient-list',
  imports: [CommonModule],
  template: `
    <div *ngIf="loading">Loading...</div>
    <div *ngIf="error" role="alert">{{ error }}</div>
    <ul *ngIf="!loading && !error">
      <li *ngFor="let p of data">{{ p.name }}</li>
    </ul>
  `
})
class PatientListComponent {
  data: Patient[] = [];
  loading = true;
  error = '';
  private api = inject(PatientsService);
  constructor() {
    this.api.list().subscribe({
      next: d => { this.data = d; this.loading = false; },
      error: () => { this.error = 'Failed'; this.loading = false; }
    });
  }
}

describe('PatientListComponent', () => {
  it('renders loading → success → error', async () => {
    const { fixture } = await render(PatientListComponent, { imports: [HttpClientTestingModule] });
    const http = TestBed.inject(HttpTestingController);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
    http.expectOne('/api/patients').flush([{ id: 'P1', name: 'Rima', ward: 'ICU' }]);
    fixture.detectChanges();
    expect(await screen.findByText('Rima')).toBeInTheDocument();
  });

  it('shows error', async () => {
    const { fixture } = await render(PatientListComponent, { imports: [HttpClientTestingModule] });
    const http = TestBed.inject(HttpTestingController);
    http.expectOne('/api/patients').flush('err', { status: 500, statusText: 'Server' });
    fixture.detectChanges();
    expect(await screen.findByRole('alert')).toHaveTextContent('Failed');
  });
});
```

## Try it
- Beginner: expectOne URL mismatch করিয়ে দেখুন টেস্ট কিভাবে fail করে।
- Advanced: দুইটি sequential API কল (list + stats) mock করে order assert করুন।

## Common mistakes
- httpMock.verify() না ডাকা → pending request এড়ায় না।
- error case টেস্ট না করা।

## Interview points
- HttpTestingController success+error; loading→success→error UI test।

## Done when…
- Success + error HTTP টেস্ট লেখা।
- Component loading state assert হয়েছে।
- httpMock.verify ব্যবহার।
