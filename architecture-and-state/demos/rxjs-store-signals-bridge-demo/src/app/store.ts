import { BehaviorSubject, Observable } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';

export type Todo = { id: string; title: string; done: boolean };
export type TodoState = { todos: Todo[] };

export class TodoStore {
  private state$ = new BehaviorSubject<TodoState>({ todos: [] });

  // write APIs
  add(title: string) {
    const todo: Todo = { id: crypto.randomUUID(), title, done: false };
    const next = { todos: [...this.state$.value.todos, todo] };
    this.state$.next(next);
  }

  toggle(id: string) {
    const next = {
      todos: this.state$.value.todos.map(t =>
        t.id === id ? { ...t, done: !t.done } : t
      )
    };
    this.state$.next(next);
  }

  // read/select APIs
  selectAll(): Observable<Todo[]> {
    return this.state$.pipe(map(s => s.todos), distinctUntilChanged());
  }
}
