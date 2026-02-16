import { BehaviorSubject, Subject, filter, map, switchMap } from 'rxjs';
import { Actions, Action } from './actions';
import { reducer, initialState } from './reducer';
import { FakeApi } from './fake-api';
import { ViewState } from './types';

export class Store {
  private state$ = new BehaviorSubject<ViewState>(initialState);
  private actions$ = new Subject<Action>();
  private subs: Array<{ unsubscribe: () => void }> = [];
  private failNext = false;

  constructor(private api: FakeApi) {
    // reducer pipeline
    const sub = this.actions$
      .pipe(map((action: Action) => reducer(this.state$.value, action)))
      .subscribe((next: ViewState) => this.state$.next(next));
    this.subs.push(sub);

    // effect: load page
    const loadEffect = this.actions$
      .pipe(filter((a: Action): a is Extract<Action, { type: 'load page' }> => a.type === 'load page'))
      .pipe(
        switchMap((a) =>
          this.api
            .list(a.page, a.pageSize, a.filter, this.failNext)
            .then(
              ({ data, total }) => Actions.loadSuccess(data, total, a.page, a.pageSize, a.filter),
              err => Actions.loadFailure(err.message ?? 'Failed')
            )
        )
      )
      .subscribe((nextAction: Action) => this.dispatch(nextAction));
    this.subs.push(loadEffect);
  }

  dispatch(action: Action) { this.actions$.next(action); }

  select(): ViewState { return this.state$.value; }

  setFailNext(v: boolean) { this.failNext = v; }

  destroy() { this.subs.forEach(s => s.unsubscribe()); this.actions$.complete(); this.state$.complete(); }
}
