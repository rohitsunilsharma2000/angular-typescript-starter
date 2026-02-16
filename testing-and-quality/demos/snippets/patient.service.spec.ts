import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

type Patient = { id: string; name: string };

@Injectable({ providedIn: 'root' })
class PatientService {
  constructor(private http: HttpClient) {}
  get(id: string) { return this.http.get<Patient>(`/api/patients/${id}`); }
}

describe('PatientService', () => {
  let http: HttpTestingController;
  let svc: PatientService;
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule], providers: [PatientService] });
    http = TestBed.inject(HttpTestingController);
    svc = TestBed.inject(PatientService);
  });
  afterEach(() => http.verify());

  it('returns patient', () => {
    let resp: Patient | undefined;
    svc.get('P1').subscribe(r => resp = r);
    const req = http.expectOne('/api/patients/P1');
    req.flush({ id: 'P1', name: 'Rima' });
    expect(resp).toEqual({ id: 'P1', name: 'Rima' });
  });

  it('handles error', () => {
    let error: any;
    svc.get('P1').subscribe({ error: e => error = e });
    const req = http.expectOne('/api/patients/P1');
    req.flush('nope', { status: 404, statusText: 'Not Found' });
    expect(error.status).toBe(404);
  });
});
