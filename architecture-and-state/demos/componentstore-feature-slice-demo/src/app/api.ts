import { Todo } from './types';
import { delay, of, throwError } from 'rxjs';

export class TodoApi {
  private todos: Todo[] = [
    { id: 't1', title: 'Admit patient', done: false },
    { id: 't2', title: 'Prep discharge', done: false }
  ];

  list() {
    return of(this.todos).pipe(delay(40));
  }

  add(title: string, shouldFail = false) {
    if (shouldFail) {
      return throwError(() => new Error('Network 500'));
    }
    this.todos = [...this.todos, { id: crypto.randomUUID(), title, done: false }];
    return of(this.todos).pipe(delay(40));
  }
}
