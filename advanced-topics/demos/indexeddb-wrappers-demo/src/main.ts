// Minimal async wrapper (Map used instead of real IndexedDB)
class Db {
  private store = new Map<string, any>();
  async put(storeName: string, key: string, value: any) {
    const bucket = this.store.get(storeName) ?? new Map();
    bucket.set(key, value);
    this.store.set(storeName, bucket);
  }
  async get(storeName: string, key: string) {
    return this.store.get(storeName)?.get(key);
  }
}

(async () => {
  const db = new Db();
  await db.put('patients', 'p1', { id: 'p1', name: 'Ayman' });
  console.log('fetch:', await db.get('patients', 'p1'));
})();
