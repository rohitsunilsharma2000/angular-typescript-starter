# 11) WebSockets with RxJS

Appointment waiting-room live queue আপডেট দিতে WebSocket দরকার; RxJS দিয়ে reconnect/backoff শিখুন।

## Why this matters (real-world)
- Live status না এলে ফ্রন্টডেস্ক ভুল তথ্য দেয়।
- Disconnect হলে silent failure সমস্যা।

## Concepts
### Beginner
- `webSocket` (rxjs/webSocket) basic connect; message stream।
### Intermediate
- Reconnect with backoff; heartbeats; close codes।
### Advanced
- Topic routing (join room), offline fallback, auth token attach।

## Copy-paste Example
```ts
// websocket-waiting-room.service.ts
import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { retryBackoff } from 'backoff-rxjs';
import { shareReplay } from 'rxjs/operators';

type Event = { type: 'ping' } | { type: 'queue', patients: number };

@Injectable({ providedIn: 'root' })
export class WaitingRoomSocket {
  private socket?: WebSocketSubject<Event>;
  stream$ = this.connect();

  private connect() {
    this.socket = webSocket<Event>({ url: 'wss://api.hms.example.com/ws/waiting-room' });
    return this.socket.pipe(
      retryBackoff({ initialInterval: 1000, maxInterval: 10000, resetOnSuccess: true }),
      shareReplay({ bufferSize: 1, refCount: true })
    );
  }

  send(msg: Event) { this.socket?.next(msg); }
}
```
```ts
// websocket-widget.component.ts
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
```

## Try it
- Beginner: console.log events; disconnect server করে backoff দেখুন।
- Advanced: auth token query param যোগ করুন এবং refresh হলে reconnect করুন।

## Common mistakes
- retry without backoff → tight loop।
- shareReplay বাদ দিয়ে বহু subscription এ multiple sockets।

## Interview points
- webSocket + retryBackoff + shareReplay; auth token attach।

## Done when…
- Socket reconnect/backoff কাজ করে।
- Widget live count দেখায়; multiple subscribers হলে single connection।
