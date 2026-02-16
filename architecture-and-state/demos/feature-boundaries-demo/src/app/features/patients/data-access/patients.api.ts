// Minimal stub so `tsc --noEmit`/VS Code doesn't complain.
export type PatientDto = { id: string; name: string; bed: string };

export class PatientApi {
  async fetchAll(): Promise<PatientDto[]> {
    // swap with real HttpClient call in an Angular app
    return [
      { id: '1', name: 'Jane Doe', bed: 'B-12' },
      { id: '2', name: 'John Smith', bed: 'A-03' }
    ];
  }
}
