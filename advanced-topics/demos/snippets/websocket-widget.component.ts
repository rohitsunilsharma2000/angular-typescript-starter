import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WaitingRoomSocket } from './websocket-waiting-room.service';

@Component({
  standalone: true,
  selector: 'hms-waiting-room-widget',
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p *ngIf="count === null">Connecting…</p>
    <p *ngIf="count !== null">Waiting patients: {{ count }}</p>
  `
})
export class WaitingRoomWidgetComponent {
  count: number | null = null;
  socket = inject(WaitingRoomSocket);
  constructor() {
    this.socket.stream$.subscribe(ev => {
      if (ev.type === 'queue') this.count = ev.patients;
    });
  }
}
