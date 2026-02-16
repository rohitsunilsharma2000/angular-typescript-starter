import { render, screen } from '@testing-library/angular';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestBed } from '@angular/core/testing';
import { Injectable } from '@angular/core';

export type Patient = { id: string; name: string; ward: string };

@Injectable({ providedIn: 'root' })
class PatientsService {
  constructor(private http: HttpClient) {}
  list() { return this.http.get<Patient[]>('/api/patients'); }
}

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
  it('loading → success', async () => {
    const { fixture } = await render(PatientListComponent, { imports: [HttpClientTestingModule] });
    const http = TestBed.inject(HttpTestingController);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
    http.expectOne('/api/patients').flush([{ id: 'P1', name: 'Rima', ward: 'ICU' }]);
    fixture.detectChanges();
    expect(await screen.findByText('Rima')).toBeInTheDocument();
  });

  it('error state', async () => {
    const { fixture } = await render(PatientListComponent, { imports: [HttpClientTestingModule] });
    const http = TestBed.inject(HttpTestingController);
    http.expectOne('/api/patients').flush('err', { status: 500, statusText: 'Server' });
    fixture.detectChanges();
    expect(await screen.findByRole('alert')).toHaveTextContent('Failed');
  });
});
