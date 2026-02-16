import { BehaviorSubject, Subject, filter, map, merge, of, switchMap, catchError } from 'rxjs';
import { Action, AppointmentsActions } from './actions';
import { FakeApi } from './fake-api';
import { reducer, initialState } from './reducer';
import { State } from './types';

export class Store {
  private state$ = new BehaviorSubject<State>(initialState);
  private actions$ = new Subject<Action>();
  private subscriptions = [] as Array<{ unsubscribe: () => void }>;

  constructor(private api: FakeApi) {
    // Reducer pipeline
    const sub = this.actions$
      .pipe(map(action => ({ action, nextState: reducer(this.state$.value, action) })))
      .subscribe(({ nextState }) => this.state$.next(nextState));
    this.subscriptions.push(sub);

    // Effect: load
    const loadEffect = this.actions$
      .pipe(filter(a => a.type === 'load'))
      .pipe(
        switchMap(() =>
          this.api.list(this.shouldFail).then(
            data => AppointmentsActions.loadSuccess(data),
            err => AppointmentsActions.loadFailure(err.message ?? 'Failed')
          )
        )
      )
      .subscribe(a => this.dispatch(a));
    this.subscriptions.push(loadEffect);
  }

  // flag to simulate failure on second call
  private shouldFail = false;
  setFailNext(value: boolean) { this.shouldFail = value; }

  dispatch(action: Action) {
    this.actions$.next(action);
  }

  select(): State {
    return this.state$.value;
  }

  destroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.actions$.complete();
    this.state$.complete();
  }
}
