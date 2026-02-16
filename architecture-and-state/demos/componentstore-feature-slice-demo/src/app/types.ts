export type Todo = { id: string; title: string; done: boolean };
export type ViewState = {
  todos: Todo[];
  loading: 'idle' | 'loading' | 'saving';
  error?: string;
};
