export type LoadingState = 'idle' | 'loading' | 'refreshing' | 'saving' | 'deleting';
export type ErrorModel = {
  code?: string;
  message: string;
  correlationId?: string;
  fieldErrors?: Record<string, string>;
};
export type ViewState<T> = {
  data: T;
  loading: LoadingState;
  error?: ErrorModel;
  lastUpdated?: number;
};
