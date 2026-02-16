import { FakeApi } from './app/api';
import { TodoStore } from './app/store';

function logState(label: string, state: ReturnType<TodoStore['snapshot']>) {
  console.log(`\n=== ${label} ===`);
  console.log({ loading: state.loading, error: state.error, todos: state.todos });
}

async function run() {
  const store = new TodoStore(new FakeApi());

  await store.load();
  logState('after load', store.snapshot());

  // optimistic success
  await store.toggle('t1', false);
  logState('after optimistic success', store.snapshot());

  // optimistic fail + rollback
  await store.toggle('t2', true);
  logState('after optimistic fail+rollback', store.snapshot());
}

run();
