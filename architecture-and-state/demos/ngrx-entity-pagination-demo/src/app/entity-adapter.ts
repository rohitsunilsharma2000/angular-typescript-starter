// Minimal NgRx Entity-like helpers
export type EntityState<T extends { id: string }> = { ids: string[]; entities: Record<string, T> };

export function getInitialState<T extends { id: string }>(): EntityState<T> {
  return { ids: [], entities: {} };
}

export function setAll<T extends { id: string }>(state: EntityState<T>, items: T[]): EntityState<T> {
  const ids = items.map(i => i.id);
  const entities: Record<string, T> = {};
  items.forEach(i => (entities[i.id] = i));
  return { ids, entities };
}

export function addMany<T extends { id: string }>(state: EntityState<T>, items: T[]): EntityState<T> {
  const ids = [...state.ids];
  const entities = { ...state.entities } as Record<string, T>;
  for (const item of items) {
    if (!entities[item.id]) ids.push(item.id);
    entities[item.id] = item;
  }
  return { ids, entities };
}

export function removeOne<T extends { id: string }>(state: EntityState<T>, id: string): EntityState<T> {
  const ids = state.ids.filter(x => x !== id);
  const { [id]: _, ...entities } = state.entities;
  return { ids, entities } as EntityState<T>;
}
