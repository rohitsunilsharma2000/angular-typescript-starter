import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpInterceptorFn } from '@angular/common/http';
import { UserContextService } from './user-context.service';
import { Cart, MenuItem, Order, Restaurant } from './models';

const API = 'http://localhost:8080/api';

export const apiHeadersInterceptor: HttpInterceptorFn = (req, next) => {
  const uc = inject(UserContextService);
  const userId = uc.userId;

  // attach X-User-Id for all calls (backend requires for cart/orders)
  const cloned = req.clone({
    setHeaders: { 'X-User-Id': String(userId) },
  });

  return next(cloned);
};

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  restaurants(params: any) {
    return this.http.get<any>(`${API}/restaurants`, { params });
  }

  restaurant(id: number) {
    return this.http.get<Restaurant>(`${API}/restaurants/${id}`);
  }

  menu(restaurantId: number) {
    return this.http.get<MenuItem[]>(`${API}/restaurants/${restaurantId}/menu`);
  }

  getCart() {
    return this.http.get<Cart>(`${API}/cart`);
  }

  upsertCartItem(menuItemId: number, qty: number) {
    return this.http.put<Cart>(`${API}/cart/items`, { menuItemId, qty });
  }

  removeCartItem(menuItemId: number) {
    return this.http.delete<Cart>(`${API}/cart/items/${menuItemId}`);
  }

  createOrder(addressId: number) {
    return this.http.post<Order>(`${API}/orders`, { addressId });
  }

  orders() {
    return this.http.get<Order[]>(`${API}/orders`);
  }

  order(id: number) {
    return this.http.get<Order>(`${API}/orders/${id}`);
  }

  confirmPayment(orderId: number, result: 'SUCCESS' | 'FAIL') {
    return this.http.post<any>(`${API}/payments/${orderId}/confirm`, null, { params: { result } });
  }
}
