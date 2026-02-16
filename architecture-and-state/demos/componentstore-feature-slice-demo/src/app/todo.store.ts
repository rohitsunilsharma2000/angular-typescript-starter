import { ComponentStore } from './component-store';
import { TodoApi } from './api';
import { Todo, ViewState } from './types';
import { catchError, of, switchMap, tap } from 'rxjs';

export class TodoStore extends ComponentStore<ViewState> {
  constructor(private api: TodoApi) {
    super({ todos: [], loading: 'idle' });
    this.loadTodos(); // kick off initial load
  }

  // selectors
  readonly todos$ = this.select(s => s.todos);
  readonly vm$ = this.select(s => s);

  // updaters
  readonly setTodos = this.updater((state, todos: Todo[]) => ({ ...state, todos }));
  readonly setLoading = this.updater((state, loading: ViewState['loading']) => ({ ...state, loading }));
  readonly setError = this.updater((state, error?: string) => ({ ...state, error }));

  // effects
  readonly loadTodos = this.effect<void>(origin$ =>
    origin$.pipe(
      tap(() => {
        this.setLoading('loading');
        this.setError(undefined);
      }),
      switchMap(() =>
        this.api.list().pipe(
          tap(todos => this.setTodos(todos)),
          tap(() => this.setLoading('idle')),
          catchError(err => {
            this.setError(err.message ?? 'Failed to load');
            this.setLoading('idle');
            return of([]);
          })
        )
      )
    )
  );

  readonly addTodo = this.effect<{ title: string; shouldFail?: boolean }>(origin$ =>
    origin$.pipe(
      tap(() => {
        this.setLoading('saving');
        this.setError(undefined);
      }),
      switchMap(({ title, shouldFail }) =>
        this.api.add(title, !!shouldFail).pipe(
          tap(todos => this.setTodos(todos)),
          tap(() => this.setLoading('idle')),
          catchError(err => {
            this.setError(err.message ?? 'Failed to add');
            this.setLoading('idle');
            return of([]);
          })
        )
      )
    )
  );
}
