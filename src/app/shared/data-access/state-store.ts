// Minimal observable-ish store without rxjs dependency
export type Listener<T> = (state: T) => void;

export class StateStore<T> {
  private state: T;
  private listeners: Listener<T>[] = [];

  constructor(initial: T) {
    this.state = initial;
  }

  get snapshot(): T {
    return this.state;
  }

  set(next: T): void {
    this.state = next;
    this.listeners.forEach(l => l(next));
  }

  subscribe(listener: Listener<T>): () => void {
    this.listeners.push(listener);
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }
}
