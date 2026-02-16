import { interval, map, take, tap, share } from 'rxjs';

// Simulated WebSocket stream
const socket$ = interval(20).pipe(
  take(3),
  map(i => ({ type: i === 0 ? 'connected' : 'update', payload: i })),
  tap(msg => console.log('incoming:', msg)),
  share()
);

socket$.subscribe(); // start stream
