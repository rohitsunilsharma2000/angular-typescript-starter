import { LoadingState, ViewState, ErrorModel } from './state.model';
import { randomUUID } from 'node:crypto';

export type Invoice = { id: string; amount: number; patient: string };

const wait = (ms: number) => new Promise(res => setTimeout(res, ms));

export class BillingStore {
  private state: ViewState<Invoice[]> = { data: [], loading: 'idle' };

  snapshot(): ViewState<Invoice[]> {
    return { ...this.state, data: [...this.state.data] };
  }

  private set(state: Partial<ViewState<Invoice[]>>) {
    this.state = { ...this.state, ...state };
  }

  async load(simulateError = false): Promise<void> {
    const nextLoading: LoadingState = this.state.data.length ? 'refreshing' : 'loading';
    this.set({ loading: nextLoading, error: undefined });
    await wait(50);
    if (simulateError) {
      const error: ErrorModel = { message: 'Server 500: failed to load invoices', code: 'E500', correlationId: randomUUID() };
      this.set({ loading: 'idle', error });
      return;
    }
    const data: Invoice[] = [
      { id: 'inv-1', amount: 1200, patient: 'Ayman' },
      { id: 'inv-2', amount: 850, patient: 'Nadia' }
    ];
    this.set({ data, loading: 'idle', lastUpdated: Date.now(), error: undefined });
  }

  async save(amount: number, patient: string): Promise<void> {
    this.set({ loading: 'saving', error: undefined });
    await wait(30);
    const newInvoice: Invoice = { id: randomUUID(), amount, patient };
    this.set({ data: [...this.state.data, newInvoice], loading: 'idle', lastUpdated: Date.now() });
  }
}
