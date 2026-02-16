# 13) IndexedDB wrappers

Offline draft (patient intake, billing form) রাখতে IndexedDB দরকার; idb/dexie দিয়ে সহজে করুন।

## Why this matters (real-world)
- নেটওয়ার্ক ডাউন হলে ড্রাফট হারাবে না।
- Sync হলে রোগীর তথ্য হঠাৎ ওভাররাইট এড়ানো যায়।

## Concepts
### Beginner
- IndexedDB basics; idb/dexie wrapper।
- Store versioning।
### Intermediate
- CRUD helper; date serialization; clear on logout।
### Advanced
- Migration strategy; conflict resolution hints; size/quotas।

## Copy-paste Example
```ts
// indexeddb-cache.service.ts (idb)
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
```

## Try it
- Beginner: draft save/load পরীক্ষা করুন।
- Advanced: version 2 migration লিখুন (new store for offline-queue)।

## Common mistakes
- Version bump না করলে schema mismatch।
- logout এ draft না মুছে অন্য ইউজার ডেটা দেখা।

## Interview points
- idb/dexie wrapper; versioning/migration; logout clear।

## Done when…
- Draft save/load কাজ করে।
- Migration পরিকল্পিত; logout clear documented।
