import { Injectable, signal, computed } from '@angular/core';
import { PatientApi, PatientDto } from './patients.api';

@Injectable({ providedIn: 'root' })
export class PatientsFacade {
  private patients = signal<PatientDto[]>([]);
  vm = computed(() => ({ patients: this.patients() }));

  constructor(private api: PatientApi) {}

  async load(): Promise<void> {
    const data = await this.api.fetchAll();
    this.patients.set(data);
  }
}
