import { Patient } from './types';

const wait = (ms: number) => new Promise(res => setTimeout(res, ms));

const MASTER: Patient[] = Array.from({ length: 25 }).map((_, idx) => ({
  id: `p${idx + 1}`,
  name: `Patient ${idx + 1}`,
  ward: idx % 2 === 0 ? 'A' : 'B'
}));

export class FakeApi {
  async list(page: number, pageSize: number, filter = '', fail = false): Promise<{ data: Patient[]; total: number }> {
    await wait(40);
    if (fail) throw new Error('Server 500');
    const filtered = filter ? MASTER.filter(p => p.name.toLowerCase().includes(filter.toLowerCase())) : MASTER;
    const start = (page - 1) * pageSize;
    const data = filtered.slice(start, start + pageSize);
    return { data, total: filtered.length };
  }
}
