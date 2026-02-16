export type ProductVM = { id: string; name: string; price: number };

// Presentational: only renders data and emits events via callbacks
export class ProductsListComponent {
  constructor(private props: { products: ProductVM[]; onSelect: (id: string) => void }) {}

  render(): string {
    if (!this.props.products.length) return 'No products yet';
    return this.props.products.map(p => `• ${p.name} ($${p.price.toFixed(2)})`).join('\n');
  }

  clickFirst(): void {
    const first = this.props.products[0];
    if (first) this.props.onSelect(first.id);
  }
}
