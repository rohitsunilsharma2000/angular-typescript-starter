# 08) Component harnesses (CDK) + complex UI

Dialog/menu/table মতো জটিল UI স্থিতিশীলভাবে টেস্ট করতে CDK Test Harness ব্যবহার করুন; HMS billing dialog উদাহরণ।

## Why this matters (real-world)
- DOM পরিবর্তনে brittle টেস্ট ভাঙে না।
- Keyboard/a11y আচরণ নিশ্চিত করা সহজ।

## Concepts
### Beginner
- Harness কী, `@angular/cdk/testing` basics।
### Intermediate
- Material harness উদাহরণ: MatDialogHarness, MatMenuHarness।
- Custom harness লেখা।
### Advanced
- Harness + ATL মিশ্রণ; async content; stable locator strategy।

## Copy-paste Example
```ts
// billing-dialog.component.ts
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
@Component({
  standalone: true,
  selector: 'hms-billing-dialog',
  imports: [MatDialogModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<h1 mat-dialog-title>Invoice {{invoiceId}}</h1>
  <div mat-dialog-content>Total: {{amount}}</div>
  <div mat-dialog-actions><button mat-button mat-dialog-close>Close</button></div>`
})
export class BillingDialogComponent { @Input() invoiceId = ''; @Input() amount = 0; }
```
```ts
// billing-dialog.harness.ts (custom harness example)
import { ComponentHarness } from '@angular/cdk/testing';
export class BillingDialogHarness extends ComponentHarness {
  static hostSelector = 'hms-billing-dialog';
  async title() { return (await this.locatorFor('h1'))().textContent; }
}
```
```ts
// billing-dialog.component.spec.ts using Material harness
import { TestBed } from '@angular/core/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialogHarness } from '@angular/material/dialog/testing';
import { HarnessLoader } from '@angular/cdk/testing';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { Component } from '@angular/core';
import { BillingDialogComponent } from './billing-dialog.component';

describe('BillingDialogComponent', () => {
  let loader: HarnessLoader;
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [MatDialogModule, BillingDialogComponent] });
    const fixture = TestBed.createComponent(BillingDialogComponent);
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.componentInstance.invoiceId = 'INV-1';
    fixture.componentInstance.amount = 500;
    fixture.detectChanges();
  });
  it('shows invoice id', async () => {
    const dialog = await loader.getHarness(MatDialogHarness);
    const title = await dialog.getTitleText();
    expect(title).toContain('INV-1');
  });
});
```

## Try it
- Beginner: MatMenuHarness দিয়ে menu item ক্লিক assert করুন।
- Advanced: custom harnessে ARIA role ব্যবহার করে locator সেট করুন।

## Common mistakes
- harness environment ভুল (testbed বনাম protractor)।
- text selector brittle; role/aria ব্যবহার না করা।

## Interview points
- Harness reduces brittleness; works with Material; custom harness possible।

## Done when…
- অন্তত একটি harness ভিত্তিক টেস্ট চলে।
- Role/aria ভিত্তিক locator ব্যবহার।
