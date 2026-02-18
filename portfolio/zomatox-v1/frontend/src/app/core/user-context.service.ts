import { Injectable } from '@angular/core';

export type DemoUser = { id: number; label: string };

@Injectable({ providedIn: 'root' })
export class UserContextService {
  // seeded: user@zomatox.local -> id 1, admin@zomatox.local -> id 2
  users: DemoUser[] = [
    { id: 1, label: 'User (id=1)' },
    { id: 2, label: 'Admin (id=2)' },
  ];

  private key = 'zomatox.userId';

  get userId(): number {
    const v = localStorage.getItem(this.key);
    return v ? Number(v) : 1;
  }

  set userId(id: number) {
    localStorage.setItem(this.key, String(id));
  }
}
