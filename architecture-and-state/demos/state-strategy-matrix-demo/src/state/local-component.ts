// Local (component-only) state: best for isolated widgets
export class LocalCounterComponent {
  private count = 0;

  increment() {
    this.count++;
  }

  render(): string {
    return `Local count: ${this.count}`;
  }
}
