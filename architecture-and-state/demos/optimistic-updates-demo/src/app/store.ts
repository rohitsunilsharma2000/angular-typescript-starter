import { FakeApi } from './api';
import { Todo } from './types';

export type ViewState = {
  todos: Todo[];
  loading: 'idle' | 'loading' | 'optimistic' | 'rollback';
  error?: string;
};

export class TodoStore {
  private state: ViewState = { todos: [], loading: 'idle' };
  constructor(private api: FakeApi) {}

  snapshot(): ViewState {
    return { ...this.state, todos: [...this.state.todos] };
  }

  async load(): Promise<void> {
    this.set({ loading: 'loading', error: undefined });
    this.set({ todos: await this.api.list(), loading: 'idle' });
  }

  async toggle(id: string, simulateFail = false): Promise<void> {
    const prev = this.snapshot();
    // optimistic update
    this.set({
      todos: this.state.todos.map(t => (t.id === id ? { ...t, done: !t.done } : t)),
      loading: 'optimistic',
      error: undefined
    });

    try {
      const serverTodos = await this.api.toggle(id, simulateFail);
      this.set({ todos: serverTodos, loading: 'idle' });
    } catch (err: any) {
      // rollback
      this.set({ ...prev, loading: 'rollback', error: err?.message ?? 'Failed' });
      // after rollback, return to idle
      this.set({ ...this.state, loading: 'idle' });
    }
  }

  private set(patch: Partial<ViewState>) {
    this.state = { ...this.state, ...patch };
  }
}
