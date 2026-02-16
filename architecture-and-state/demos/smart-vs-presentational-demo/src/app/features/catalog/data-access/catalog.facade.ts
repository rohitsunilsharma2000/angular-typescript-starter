import { CatalogApi, ProductDto } from './catalog.api';

export type CatalogVM = { products: ProductDto[] };

// Smart data layer: fetch + adapt
export class CatalogFacade {
  private products: ProductDto[] = [];

  constructor(private api: CatalogApi) {}

  async load(): Promise<void> {
    this.products = await this.api.fetchAll();
  }

  vm(): CatalogVM {
    return { products: this.products };
  }
}
