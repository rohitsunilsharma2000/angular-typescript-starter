import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

interface AppointmentPayload {
  patient: string;
  doctor: string;
  slot: string;
  notes?: string;
}

interface PatientDto { id: number; firstName: string; lastName: string; email: string; }

@Component({
  selector: 'hms-appointments',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './hms-appointments.component.html',
  styleUrls: ['./hms-appointments.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HmsAppointmentsComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  loading = false;
  patients: PatientDto[] = [];
  message = '';

  form = this.fb.group({
    patient: ['', Validators.required],
    doctor: ['Dr. Sen', Validators.required],
    slot: ['', Validators.required],
    notes: [''],
  });

  ngOnInit() {
    this.fetchPatients();
  }

  fetchPatients() {
    this.loading = true;
    this.http.get<{ users: PatientDto[] }>('https://dummyjson.com/users?limit=6')
      .pipe(tap(() => (this.loading = false)))
      .subscribe({
        next: res => this.patients = res.users,
        error: () => { this.message = 'Patients load failed'; this.loading = false; },
      });
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    const payload: AppointmentPayload = this.form.getRawValue();
    this.http.post('https://jsonplaceholder.typicode.com/posts', payload)
      .pipe(tap(() => (this.loading = false)))
      .subscribe({
        next: res => this.message = 'Mock appointment created (id: ' + (res as any).id + ')',
        error: () => this.message = 'Failed to create appointment',
      });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigateByUrl('/');
  }
}
