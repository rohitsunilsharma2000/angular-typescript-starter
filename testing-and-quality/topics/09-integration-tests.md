# 09) Integration tests (feature route level)

Appointments feature route + mocked API একসাথে চালিয়ে behavior যাচাই করুন (loading + success + navigation)।

## Why this matters (real-world)
- Unit pass করলেও wiring ভুল হতে পারে।
- Mocked networkে deterministic, বাস্তব রাউটিংসহ।

## Concepts
### Beginner
- provideRouter + HttpClientTestingModule সহ route render।
- Page-level assertion (heading, list)।
### Intermediate
- Multiple API calls stub; navigation within feature।
### Advanced
- Contract test for query params; fixture data builders; slow network simulation।

## Copy-paste Example
```ts
// appointments.route.spec.ts
import { render, screen } from '@testing-library/angular';
import { provideRouter, Routes } from '@angular/router';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-appointments-page',
  template: `<h1>Appointments</h1>
    <div *ngIf="loading">Loading</div>
    <ul *ngIf="!loading">
      <li *ngFor="let a of data">{{a.patient}}</li>
    </ul>`
})
class AppointmentsPage {
  data: any[] = [];
  loading = true;
  constructor(http: HttpClient) {
    http.get<any[]>('/api/appointments').subscribe(res => { this.data = res; this.loading = false; });
  }
}

const routes: Routes = [{ path: 'appointments', component: AppointmentsPage }];

describe('Appointments route integration', () => {
  it('renders list via mocked API', async () => {
    const { fixture } = await render(AppointmentsPage, { imports: [HttpClientTestingModule], providers: [provideRouter(routes)] });
    const http = TestBed.inject(HttpTestingController);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
    http.expectOne('/api/appointments').flush([{ patient: 'Rima' }]);
    fixture.detectChanges();
    expect(await screen.findByText('Rima')).toBeInTheDocument();
  });
});
```

## Try it
- Beginner: second API (stats) যোগ করুন ও stub করুন।
- Advanced: slow network simulate (setTimeout) ও loading indicator ধরে রাখুন।

## Common mistakes
- Router providers ভুলে component standalone import ব্যর্থ।
- HttpTestingController.verify() ভুলে pending request।

## Interview points
- Feature-level render with router + mocked HTTP; near-real wiring।

## Done when…
- Route-level integration টেস্ট চলে।
- Loading → success path asserted।
- All pending requests verify।
