import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Appointment {
  id: string;
  patient: string;
  slot: string;
}

@Component({
  standalone: true,
  selector: 'hms-onpush-hot-list',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul>
      <li *ngFor="let a of items; trackBy: trackById">
        {{ a.patient }} — {{ a.slot }}
      </li>
    </ul>
  `
})
export class OnpushHotListComponent {
  @Input({ required: true }) items: Appointment[] = [];
  trackById = (_: number, a: Appointment) => a.id;
}
