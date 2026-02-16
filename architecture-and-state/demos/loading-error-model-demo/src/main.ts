import { BillingStore } from './app/billing.store';

function logState(label: string, state: ReturnType<BillingStore['snapshot']>) {
  console.log(`\n=== ${label} ===`);
  console.log({
    loading: state.loading,
    error: state.error,
    data: state.data,
    lastUpdated: state.lastUpdated
  });
}

async function run() {
  const store = new BillingStore();

  logState('initial', store.snapshot());

  await store.load();
  logState('after load success', store.snapshot());

  await store.load(true);
  logState('after load error', store.snapshot());

  await store.save(499, 'Sakib');
  logState('after save', store.snapshot());
}

run();
