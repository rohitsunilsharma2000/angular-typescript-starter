import { render, screen } from '@testing-library/angular';
import { provideRouter, Router, RouterModule } from '@angular/router';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'hms-billing-link',
  imports: [RouterModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<button (click)="go()">Go Billing</button>`
})
class BillingLinkComponent {
  constructor(private router: Router) {}
  go() { this.router.navigate(['/billing', 'invoice-1']); }
}

describe('Router navigation', () => {
  it('navigates to billing', async () => {
    const { fixture } = await render(BillingLinkComponent, { providers: [provideRouter([])] });
    const router = fixture.componentRef.injector.get(Router);
    const spy = vi.spyOn(router, 'navigate');
    await userEvent.click(screen.getByRole('button', { name: /billing/i }));
    expect(spy).toHaveBeenCalledWith(['/billing', 'invoice-1']);
  });
});
