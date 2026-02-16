import { LocalCounterComponent } from './state/local-component';
import { ServiceStore } from './state/service-store';
import { EventBus } from './state/global-event-bus';

async function run() {
  console.log('--- Local component state (isolated) ---');
  const local = new LocalCounterComponent();
  local.increment();
  local.increment();
  console.log(local.render());

  console.log('\n--- Service store (feature-shared) ---');
  const store = new ServiceStore<{ count: number }>({ count: 0 });
  const unsubscribe = store.subscribe(s => console.log('Listener saw count:', s.count));
  store.update(s => ({ count: s.count + 1 }));
  store.update(s => ({ count: s.count + 1 }));
  unsubscribe();

  console.log('\n--- Global event bus (cross-feature) ---');
  const bus = new EventBus();
  bus.on('user:logged-in', payload => console.log('Feature A heard:', payload));
  bus.on('user:logged-in', payload => console.log('Feature B heard:', payload));
  bus.emit('user:logged-in', { id: 'u1', name: 'Ayman' });
}

run();
