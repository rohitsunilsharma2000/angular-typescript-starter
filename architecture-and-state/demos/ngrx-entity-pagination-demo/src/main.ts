import { Store } from './app/store';
import { Actions } from './app/actions';
import { FakeApi } from './app/fake-api';

function logState(label: string, state: ReturnType<Store['select']>) {
  console.log(`\n=== ${label} ===`);
  console.log({
    page: state.pagination.page,
    pageSize: state.pagination.pageSize,
    total: state.pagination.total,
    filter: state.pagination.filter,
    loading: state.loading,
    error: state.error,
    ids: state.data.ids,
    first: state.data.ids[0] ? state.data.entities[state.data.ids[0]] : undefined
  });
}

async function run() {
  const store = new Store(new FakeApi());

  store.dispatch(Actions.loadPage(1, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('page 1', store.select());

  store.dispatch(Actions.loadPage(2, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('page 2', store.select());

  store.dispatch(Actions.loadPage(1, 5, '5'));
  await new Promise(r => setTimeout(r, 70));
  logState('filter name contains "5"', store.select());

  store.setFailNext(true);
  store.dispatch(Actions.loadPage(1, 5, ''));
  await new Promise(r => setTimeout(r, 70));
  logState('failure (rollback keeps old data)', store.select());

  store.destroy();
}

run();
