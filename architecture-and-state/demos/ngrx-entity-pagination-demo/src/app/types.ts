export type Patient = { id: string; name: string; ward: string };
export type EntityState<T> = { ids: string[]; entities: Record<string, T> };
export type Pagination = { page: number; pageSize: number; total: number; filter: string };
export type ViewState = {
  data: EntityState<Patient>;
  pagination: Pagination;
  loading: boolean;
  error?: string;
};
