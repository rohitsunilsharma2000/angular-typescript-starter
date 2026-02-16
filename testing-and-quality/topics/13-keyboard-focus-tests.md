# 13) Keyboard + focus management tests

Dialog/menu/form-এ keyboard trap না থাকা ও focus সঠিক জায়গায় ফেরত যাওয়া নিশ্চিত করুন; HMS consent dialog উদাহরণ।

## Why this matters (real-world)
- Keyboard-only ব্যবহারকারী আটকে গেলে চিকিৎসা নথি পূরণ সম্ভব নয়।
- a11y পরিদর্শনে দ্রুত ধরা পড়ে।

## Concepts
### Beginner
- Tab order, focus-visible, role।
- ATL `userEvent.tab()` দিয়ে traversal।
### Intermediate
- Dialog focus trap; Escape closes; return focus to trigger।
### Advanced
- Complex menu/combobox keyboard interaction; focus restore after route change।

## Copy-paste Example
```ts
// consent-dialog.component.ts
import { ChangeDetectionStrategy, Component, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-consent-dialog',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button #openBtn (click)="open=true">Open consent</button>
    <div *ngIf="open" role="dialog" aria-modal="true">
      <button #closeBtn (click)="close()">Close</button>
      <button>Agree</button>
    </div>
  `
})
export class ConsentDialogComponent {
  open = false;
  @ViewChild('openBtn') openBtn!: ElementRef<HTMLButtonElement>;
  close() { this.open = false; queueMicrotask(() => this.openBtn.nativeElement.focus()); }
}
```
```ts
// consent-dialog.component.spec.ts
import { render, screen } from '@testing-library/angular';
import userEvent from '@testing-library/user-event';
import { ConsentDialogComponent } from './consent-dialog.component';

describe('ConsentDialog keyboard', () => {
  it('traps focus and returns to opener', async () => {
    await render(ConsentDialogComponent);
    await userEvent.click(screen.getByRole('button', { name: /open consent/i }));
    await userEvent.tab();
    expect(screen.getByRole('button', { name: /close/i })).toHaveFocus();
    await userEvent.tab();
    expect(screen.getByRole('button', { name: /agree/i })).toHaveFocus();
    await userEvent.keyboard('{Escape}');
    expect(screen.getByRole('button', { name: /open consent/i })).toHaveFocus();
  });
});
```

## Try it
- Beginner: Escape কাজ না করলে টেস্ট ফেল করান।
- Advanced: Menu/combobox interaction টেস্ট লিখুন (arrow keys)।

## Common mistakes
- focus ফিরিয়ে না দেওয়া; tab trap থেকে বের হতে না পারা।
- getByText দিয়ে বোতাম খোঁজা (role better)।

## Interview points
- userEvent.tab/keyboard; focus return; dialog aria-modal।

## Done when…
- Dialog/menu keyboard flow টেস্টেড।
- Escape close ও focus return asserted।
