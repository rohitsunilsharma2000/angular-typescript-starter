import { Todo } from './types';

const wait = (ms: number) => new Promise(res => setTimeout(res, ms));

export class FakeApi {
  private todos: Todo[] = [
    { id: 't1', title: 'Draft SOP', done: false },
    { id: 't2', title: 'Review PR', done: false }
  ];

  async toggle(id: string, shouldFail = false): Promise<Todo[]> {
    await wait(40);
    if (shouldFail) {
      throw new Error('Network 500');
    }
    this.todos = this.todos.map(t => (t.id === id ? { ...t, done: !t.done } : t));
    return this.todos;
  }

  async list(): Promise<Todo[]> {
    await wait(10);
    return this.todos;
  }
}
