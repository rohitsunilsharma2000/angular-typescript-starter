# 03) Angular Testing Library basics

Patient form ও list user-centric ভাবে টেস্ট করতে Angular Testing Library (ATL) ব্যবহার করুন।

## Why this matters (real-world)
- DOM implementation detail এড়িয়ে আচরণ টেস্ট হয়।
- Stable queries → কম flake।
- ইন্টারভিউ: “How do you test components behaviorally?”

## Concepts
### Beginner
- render, screen, userEvent basics; `getByRole/getByLabelText`।
- Async assertion `findByText`।
### Intermediate
- Loading → success → error UI assert।
- Form validation errors assert (required, min length)।
### Advanced
- Custom render with providers; accessibility queries; snapshot এড়িয়ে behavior।

## Copy-paste Example
```ts
// patient-form.component.ts (standalone component under test)
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
@Component({
  standalone: true,
  selector: 'hms-patient-form',
  imports: [CommonModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form #f="ngForm" (ngSubmit)="submit(f)">
      <label>Patient Name <input name="name" ngModel required /></label>
      <button type="submit">Save</button>
      <p *ngIf="error" role="alert">{{ error }}</p>
      <p *ngIf="success" class="text-green-600">Saved</p>
    </form>
  `
})
export class PatientFormComponent {
  error = '';
  success = false;
  submit(f: NgForm) {
    this.success = false;
    if (f.invalid) { this.error = 'Name required'; return; }
    this.error = '';
    this.success = true;
  }
}
```
```ts
// patient-form.component.spec.ts
import { render, screen } from '@testing-library/angular';
import userEvent from '@testing-library/user-event';
import { PatientFormComponent } from './patient-form.component';

describe('PatientFormComponent', () => {
  it('shows validation error then success', async () => {
    await render(PatientFormComponent);
    await userEvent.click(screen.getByRole('button', { name: /save/i }));
    expect(screen.getByRole('alert')).toHaveTextContent('Name required');

    await userEvent.type(screen.getByLabelText(/patient name/i), 'Rima');
    await userEvent.click(screen.getByRole('button', { name: /save/i }));
    expect(await screen.findByText('Saved')).toBeInTheDocument();
  });
});
```

## Try it
- Beginner: Placeholder text ব্যবহার করে query করার চেষ্টা করুন ও কেন role/label ভালো তা নোট করুন।
- Advanced: render helper বানিয়ে providers যুক্ত করুন (theme/service)।

## Common mistakes
- `getByTestId` অতিরিক্ত ব্যবহার → brittle।
- sync assertion where async needed (loading state)।

## Interview points
- user-centric queries; async assertions; avoid snapshots।

## Done when…
- ATL দিয়ে অন্তত এক component test চলে।
- Loading/validation/error states assert করা হয়েছে।
- Queries role/label ভিত্তিক।
