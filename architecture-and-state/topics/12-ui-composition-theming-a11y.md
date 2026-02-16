# 12) UI composition: theming + accessibility

HMS UI তে common button/input/dialog দরকার; theme tokens দিয়ে consistent look, a11y-first navigation।

## Why this matters (real world)
- Reuse করলে ডিজাইন debt কমে।
- Dark/light switch সহজ।
- ইন্টারভিউ: a11y প্রশ্ন (keyboard, aria) + design system tokens।

## Concepts (beginner → intermediate → advanced)
- Beginner: theme tokens as CSS vars; standalone UI components।
- Intermediate: tokens import in components; focus-visible, aria-label।
- Advanced: dialog with role, keyboard trap; token-based spacing/typography; Tailwind or Material interop।

## Copy-paste Example
```ts
// architecture-and-state/demos/shared-ui-kit/theme-tokens.ts
export const themeTokens = {
  colorPrimary: '#2563eb',
  colorSurface: '#ffffff',
  colorSurfaceMuted: '#f8fafc',
  textPrimary: '#0f172a',
  radius: '8px',
  spacing: '12px',
  shadow: '0 4px 12px rgba(0,0,0,0.08)'
};
```
```ts
// app/shared/ui/button/button.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { themeTokens } from 'architecture-and-state/demos/shared-ui-kit/theme-tokens';
@Component({
  standalone: true,
  selector: 'hms-button',
  imports: [CommonModule],
  host: { '[attr.role]': "'button'", '[attr.tabindex]': '0' },
  styles: [`
    :host { display: inline-flex; align-items: center; gap: 6px; cursor: pointer; padding: ${themeTokens.spacing}; border-radius: ${themeTokens.radius}; background: ${themeTokens.colorPrimary}; color: white; box-shadow: ${themeTokens.shadow}; }
    :host(:focus-visible) { outline: 2px solid black; outline-offset: 2px; }
  `],
  template: `<ng-content></ng-content>`
})
export class ButtonComponent {
  @Input() kind: 'primary' | 'ghost' = 'primary';
}
```
```ts
// app/shared/ui/dialog/dialog.component.ts (keyboard + aria)
import { Component, EventEmitter, Input, Output, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
@Component({
  standalone: true,
  selector: 'hms-dialog',
  imports: [CommonModule],
  template: `
    <div class="fixed inset-0 bg-black/30" role="presentation" (click)="close.emit()"></div>
    <section class="fixed inset-0 flex items-center justify-center" role="dialog" aria-modal="true" [attr.aria-label]="title">
      <div class="bg-white p-4 rounded shadow max-w-md w-full" (click)="$event.stopPropagation()">
        <header class="flex justify-between items-center">
          <h2 class="font-semibold">{{ title }}</h2>
          <button (click)="close.emit()" aria-label="Close">✕</button>
        </header>
        <div class="mt-3"><ng-content></ng-content></div>
      </div>
    </section>
  `
})
export class DialogComponent {
  @Input() title = 'Dialog';
  @Output() close = new EventEmitter<void>();
  @HostListener('document:keydown.escape') onEsc() { this.close.emit(); }
}
```

## Try it (exercise)
- Beginner: ButtonComponent এ `kind="ghost"` prop যোগ করে style swap করুন।
- Advanced: Dialog এ focus trap যোগ করুন (first/last focusable cycling), aria-describedby সাপোর্ট দিন।

## Common mistakes
- Tokens hardcode না করে CSS var ব্যবহার না করা।
- Dialog এ aria-modal/role বাদ দেওয়া।
- focus-visible ছাড়া keyboard users বাদ পড়ে।

## Interview points
- Theme tokens ফাইল mention করুন; CSS var vs design tokens।
- a11y: keyboard nav, aria-label, escape-close।

## Done when…
- Tokens ফাইল আছে ও কম্পোনেন্টে ব্যবহার।
- Button/Dialog accessible (role, focus-visible)।
- Token-driven spacing/color বজায়।
