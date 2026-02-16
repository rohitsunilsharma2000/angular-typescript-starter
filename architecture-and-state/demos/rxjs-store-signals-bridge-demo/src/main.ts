import { TodoStore } from './app/store';
import { fromObservable } from './app/signals-bridge';

async function run() {
  const store = new TodoStore();

  // Bridge RxJS stream -> signal-like getter
  const todosSignal = fromObservable(store.selectAll());

  console.log('--- Initial ---');
  console.log(todosSignal());

  store.add('Write docs');
  store.add('Ship demo');

  // Allow async microtasks to push values
  await new Promise(r => setTimeout(r, 0));
  console.log('\n--- After adds (signal) ---');
  console.log(todosSignal());

  const firstId = todosSignal()[0]?.id;
  if (firstId) store.toggle(firstId);
  await new Promise(r => setTimeout(r, 0));
  console.log('\n--- After toggle (signal) ---');
  console.log(todosSignal());

  todosSignal.destroy();
}

run();
