import { validatePatients } from './validator';
import { PatientDto } from './contracts';

// Simulated fetch
async function fetchPatients(kind: 'good' | 'bad'): Promise<unknown> {
  if (kind === 'good') {
    return [
      { id: 'p1', name: 'Ayman', age: 32, ward: 'A', allergies: ['penicillin'] },
      { id: 'p2', name: 'Nadia', age: 45, ward: 'B', allergies: [] }
    ];
  }
  // bad response: missing age, wrong type for ward
  return [
    { id: 'p3', name: 'Invalid', ward: 123 },
    { id: '', name: 'EmptyId', age: -1, ward: 'C', allergies: ['dust'] }
  ];
}

export async function getPatients(kind: 'good' | 'bad'): Promise<PatientDto[]> {
  const raw = await fetchPatients(kind);
  return validatePatients(raw);
}
