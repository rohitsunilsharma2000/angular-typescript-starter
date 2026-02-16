import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'hms-i18n-demo',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@apptCount">{count, plural, one {# appointment today} other {# appointments today}}</p>
    <p i18n="@@nurseGreet">{gender, select, male {He is ready} female {She is ready} other {They are ready}}</p>
  `
})
export class I18nDemoComponent {
  count = 2;
  gender: 'male' | 'female' | 'other' = 'female';
}
