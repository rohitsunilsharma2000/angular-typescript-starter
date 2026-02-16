import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { retryBackoff } from 'backoff-rxjs';
import { shareReplay } from 'rxjs/operators';

type Event = { type: 'queue', patients: number } | { type: 'ping' };

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
