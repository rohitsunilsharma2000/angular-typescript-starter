// Service-level store: share state across multiple components of one feature
export type Listener<T> = (state: T) => void;

export class ServiceStore<T> {
  private state: T;
  private listeners: Listener<T>[] = [];

  constructor(initial: T) {
    this.state = initial;
  }

  get snapshot(): T {
    return this.state;
  }

  update(mutator: (state: T) => T): void {
    this.state = mutator(this.state);
    this.listeners.forEach(l => l(this.state));
  }

  subscribe(listener: Listener<T>): () => void {
    this.listeners.push(listener);
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }
}
