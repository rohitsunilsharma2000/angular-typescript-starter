export type ProductDto = { id: string; name: string; price: number };

// API stub (simulates server call)
export class CatalogApi {
  async fetchAll(): Promise<ProductDto[]> {
    return [
      { id: 'p1', name: 'Stethoscope', price: 120 },
      { id: 'p2', name: 'Blood Pressure Cuff', price: 65 }
    ];
  }
}
