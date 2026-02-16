# 06) Mocking Router/Guards/Navigation

Appointment success হলে billing পেজে navigate করতে হবে—button ক্লিক navigation টেস্ট করা শিখুন।

## Why this matters (real-world)
- ভুল URL এ গেলে বিলিং টিম বিভ্রান্ত।
- Guards/redirect কাজ করছে কিনা নিশ্চিত করা যায়।

## Concepts
### Beginner
- `provideRouter`/RouterTestingModule দিয়ে navigation spy।
- Link/button click → URL change assert।
### Intermediate
- ActivatedRoute stub; query params; guard allowing/denying।
### Advanced
- Navigation extras (state), resolver data mock, route-level providers।

## Copy-paste Example
```ts
// billing-link.component.ts
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
@Component({
  standalone: true,
  selector: 'hms-billing-link',
  imports: [RouterModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<button (click)="go()">Go Billing</button>`
})
export class BillingLinkComponent {
  constructor(private router: Router) {}
  go() { this.router.navigate(['/billing', 'invoice-1']); }
}
```
```ts
// billing-link.component.spec.ts
import { render, screen } from '@testing-library/angular';
import { provideRouter, Router } from '@angular/router';
import userEvent from '@testing-library/user-event';
import { BillingLinkComponent } from './billing-link.component';
import { vi } from 'vitest';

describe('BillingLinkComponent', () => {
  it('navigates on click', async () => {
    const { fixture } = await render(BillingLinkComponent, { providers: [provideRouter([])] });
    const router = fixture.componentRef.injector.get(Router);
    const spy = vi.spyOn(router, 'navigate');
    await userEvent.click(screen.getByRole('button', { name: /billing/i }));
    expect(spy).toHaveBeenCalledWith(['/billing', 'invoice-1']);
  });
});
```

## Try it
- Beginner: `router.navigateByUrl('/appointments')` টেস্ট করুন।
- Advanced: guard mock করুন যা false রিটার্ন করে; expect navigation not to happen।

## Common mistakes
- Router spy না করে শুধু URL string চেক করা।
- provideRouter না দিয়ে Router null।

## Interview points
- provideRouter/RouterTestingModule, navigation spy, guard/resolver mocking।

## Done when…
- Button click → navigate test পাস।
- Guard/param handling উদাহরণ আছে বা নথিভুক্ত।
