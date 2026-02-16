// Lightweight mock HTTP client used in docs/testing
export class HttpClientService {
  async get<T>(url: string): Promise<T> {
    console.log('Mock GET', url);
    return {} as T;
  }
}
