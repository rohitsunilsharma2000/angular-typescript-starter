import { Injectable } from '@angular/core';
import { openDB, IDBPDatabase } from 'idb';

type Draft = { id: string; payload: any; updatedAt: number };

@Injectable({ providedIn: 'root' })
export class IndexedDbCacheService {
  private dbPromise: Promise<IDBPDatabase>;
  constructor() {
    this.dbPromise = openDB('hms-cache', 1, {
      upgrade(db) {
        db.createObjectStore('drafts', { keyPath: 'id' });
      },
    });
  }
  async putDraft(draft: Draft) {
    const db = await this.dbPromise;
    await db.put('drafts', draft);
  }
  async getDraft(id: string) {
    const db = await this.dbPromise;
    return db.get('drafts', id) as Promise<Draft | undefined>;
  }
  async clearAll() {
    const db = await this.dbPromise;
    await db.clear('drafts');
  }
}
