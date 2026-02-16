import { Store } from './app/store';
import { AppointmentsActions } from './app/actions';
import { FakeApi } from './app/fake-api';

function logState(label: string, state: ReturnType<Store['select']>) {
  console.log(`\n=== ${label} ===`);
  console.log(state);
}

async function run() {
  const store = new Store(new FakeApi());

  store.dispatch(AppointmentsActions.load());
  await new Promise(r => setTimeout(r, 70));
  logState('after load success', store.select());

  store.setFailNext(true);
  store.dispatch(AppointmentsActions.load());
  await new Promise(r => setTimeout(r, 70));
  logState('after load failure', store.select());

  store.destroy();
}

run();
