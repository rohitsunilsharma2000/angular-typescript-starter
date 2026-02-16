import { interval, take } from 'rxjs';

let manualLeaks = 0;

function manualSubscribe() {
  const sub = interval(10).pipe(take(3)).subscribe(v => console.log('manual', v));
  // forgot sub.unsubscribe();
  manualLeaks++;
}

function asyncPipeStyle() {
  interval(10).pipe(take(3)).subscribe(v => console.log('async-pipe-like', v));
  // In template async pipe would auto-complete
}

manualSubscribe();
asyncPipeStyle();
console.log('manual subscriptions (potential leaks):', manualLeaks);
