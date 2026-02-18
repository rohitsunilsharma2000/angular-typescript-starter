import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, of, tap } from 'rxjs';
import { ApiService } from '../../../core/api/api.service';
import { CartView, OrderView, ProductView } from '../../../core/models/api.models';

@Injectable({ providedIn: 'root' })
export class CustomerStore {
  private readonly storeIdState = new BehaviorSubject<number>(Number(localStorage.getItem('blinkit_store_id')) || 1);
  private readonly productsState = new BehaviorSubject<ProductView[]>([]);
  private readonly cartState = new BehaviorSubject<CartView | null>(null);
  private readonly ordersState = new BehaviorSubject<OrderView[]>([]);

  readonly storeId$ = this.storeIdState.asObservable();
  readonly products$ = this.productsState.asObservable();
  readonly cart$ = this.cartState.asObservable();
  readonly orders$ = this.ordersState.asObservable();

  constructor(private readonly api: ApiService) {}

  storeId(): number {
    return this.storeIdState.value;
  }

  setStoreId(storeId: number): void {
    this.storeIdState.next(storeId);
    localStorage.setItem('blinkit_store_id', String(storeId));
  }

  loadProducts(query = ''): Observable<ProductView[]> {
    return this.api.products(this.storeId(), query).pipe(tap((items) => this.productsState.next(items)));
  }

  addCartItem(productId: number, qty: number): Observable<CartView> {
    return this.api.addCartItem(productId, this.storeId(), qty).pipe(tap((cart) => this.cartState.next(cart)));
  }

  loadCart(): Observable<CartView | null> {
    return this.api.cart().pipe(
      tap((cart) => this.cartState.next(cart)),
      catchError(() => {
        this.cartState.next(null);
        return of(null);
      })
    );
  }

  removeCartItem(productId: number): Observable<void> {
    return this.api.removeCartItem(productId).pipe(tap(() => this.loadCart().subscribe()));
  }

  loadOrders(): Observable<OrderView[]> {
    return this.api.orders().pipe(tap((orders) => this.ordersState.next(orders)));
  }
}
