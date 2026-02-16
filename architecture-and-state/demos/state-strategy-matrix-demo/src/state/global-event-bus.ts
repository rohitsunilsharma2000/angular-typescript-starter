// Global event bus: use sparingly for cross-feature broadcasts
export type EventHandler = (payload: unknown) => void;

export class EventBus {
  private handlers: Record<string, EventHandler[]> = {};

  on(event: string, handler: EventHandler): () => void {
    this.handlers[event] = this.handlers[event] ?? [];
    this.handlers[event].push(handler);
    return () => {
      this.handlers[event] = this.handlers[event].filter(h => h !== handler);
    };
  }

  emit(event: string, payload: unknown): void {
    (this.handlers[event] ?? []).forEach(h => h(payload));
  }
}
