import { TodoApi } from './app/api';
import { TodoStore } from './app/todo.store';

async function run() {
  const store = new TodoStore(new TodoApi());

  // Allow initial load to finish
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after load ===');
  console.log(store.snapshot);

  store.addTodo({ title: 'Check vitals' });
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after add success ===');
  console.log(store.snapshot);

  store.addTodo({ title: 'Failing task', shouldFail: true });
  await new Promise(r => setTimeout(r, 60));
  console.log('\n=== after add failure ===');
  console.log(store.snapshot);

  // cleanup effects
  (store.loadTodos as any).destroy?.();
  (store.addTodo as any).destroy?.();
}

run();
