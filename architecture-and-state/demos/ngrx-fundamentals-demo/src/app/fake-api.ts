import { Appointment } from './types';

const wait = (ms: number) => new Promise(res => setTimeout(res, ms));

export class FakeApi {
  private data: Appointment[] = [
    { id: 'a1', patient: 'Ayman', slot: '10:00' },
    { id: 'a2', patient: 'Nadia', slot: '10:30' }
  ];

  async list(simulateFail = false): Promise<Appointment[]> {
    await wait(50);
    if (simulateFail) {
      throw new Error('Server 500');
    }
    return this.data;
  }
}
