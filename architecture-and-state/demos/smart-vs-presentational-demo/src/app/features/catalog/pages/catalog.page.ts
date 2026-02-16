import { CatalogFacade } from '../data-access/catalog.facade';
import { ProductsListComponent } from '../../../shared/ui/products-list.component';

// Smart/container: orchestrates data + user events
export class CatalogPage {
  private list?: ProductsListComponent;

  constructor(private facade: CatalogFacade) {}

  async init(): Promise<void> {
    await this.facade.load();
    this.list = new ProductsListComponent({
      products: this.facade.vm().products,
      onSelect: this.handleSelect
    });
  }

  render(): string {
    return this.list ? this.list.render() : 'Loading...';
  }

  simulateUserClick(): void {
    this.list?.clickFirst();
  }

  private handleSelect = (id: string) => {
    console.log('[event up] selected product id:', id);
  };
}
