import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CartView,
  CheckoutPreviewResponse,
  LoginRequest,
  OrderView,
  ProductView,
  StoreResolveResponse,
  TokenResponse
} from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = 'http://localhost:8080/api';

  constructor(private readonly http: HttpClient) {}

  login(payload: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.base}/auth/login`, payload);
  }

  resolveStore(pincode: string): Observable<StoreResolveResponse> {
    return this.http.get<StoreResolveResponse>(`${this.base}/stores/resolve?pincode=${pincode}`);
  }

  products(storeId: number, q = '', page = 0): Observable<ProductView[]> {
    return this.http.get<ProductView[]>(`${this.base}/products?storeId=${storeId}&q=${encodeURIComponent(q)}&page=${page}`);
  }

  product(id: number, storeId: number): Observable<ProductView> {
    return this.http.get<ProductView>(`${this.base}/products/${id}?storeId=${storeId}`);
  }

  addCartItem(productId: number, storeId: number, qty: number): Observable<CartView> {
    return this.http.put<CartView>(`${this.base}/cart/items`, { productId, storeId, qty });
  }

  cart(): Observable<CartView> {
    return this.http.get<CartView>(`${this.base}/cart`);
  }

  removeCartItem(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/cart/items/${productId}`);
  }

  checkoutPreview(storeId: number): Observable<CheckoutPreviewResponse> {
    return this.http.post<CheckoutPreviewResponse>(`${this.base}/checkout/preview`, { storeId });
  }

  placeOrder(reservationId: number, couponCode: string | null, walletAmount: number, idemKey: string): Observable<OrderView> {
    return this.http.post<OrderView>(`${this.base}/orders`, { reservationId, couponCode, walletAmount }, {
      headers: new HttpHeaders({ 'Idempotency-Key': idemKey })
    });
  }

  orders(): Observable<OrderView[]> {
    return this.http.get<OrderView[]>(`${this.base}/orders`);
  }

  orderById(orderId: number): Observable<OrderView> {
    return this.http.get<OrderView>(`${this.base}/orders/${orderId}`);
  }

  startPayment(orderId: number): Observable<void> {
    return this.http.post<void>(`${this.base}/payments/${orderId}/start`, {});
  }

  confirmPayment(orderId: number, result: 'SUCCESS' | 'FAIL'): Observable<void> {
    return this.http.post<void>(`${this.base}/payments/${orderId}/confirm?result=${result}`, {});
  }

  adminCreateProduct(payload: unknown): Observable<unknown> {
    return this.http.post(`${this.base}/admin/products`, payload);
  }

  adminUpdateProduct(id: number, payload: unknown): Observable<unknown> {
    return this.http.put(`${this.base}/admin/products/${id}`, payload);
  }

  adminUpdateInventory(storeId: number, productId: number, stockOnHand: number): Observable<void> {
    return this.http.put<void>(`${this.base}/admin/inventory/${storeId}/${productId}`, { stockOnHand });
  }

  adminOrders(status = '', storeId?: number): Observable<any[]> {
    const qp = new URLSearchParams();
    if (status) qp.append('status', status);
    if (storeId) qp.append('storeId', String(storeId));
    const qs = qp.toString();
    return this.http.get<any[]>(`${this.base}/admin/orders${qs ? `?${qs}` : ''}`);
  }

  adminAssignRider(orderId: number, riderUserId: number): Observable<void> {
    return this.http.post<void>(`${this.base}/admin/orders/${orderId}/assign-rider`, { riderUserId });
  }

  adminAdvanceStatus(orderId: number, nextStatus: string): Observable<void> {
    return this.http.post<void>(`${this.base}/admin/orders/${orderId}/advance-status`, { nextStatus });
  }

  adminRefunds(status = 'PENDING'): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/admin/refunds?status=${status}`);
  }

  adminApproveRefund(refundId: number): Observable<void> {
    return this.http.post<void>(`${this.base}/admin/refunds/${refundId}/approve`, {});
  }

  adminAudit(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/admin/audit`);
  }

  warehousePickingQueue(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/warehouse/orders?status=PICKING`);
  }

  warehousePicklist(orderId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/warehouse/orders/${orderId}/picklist`);
  }

  warehouseMarkMissing(orderId: number, orderItemId: number, missingQty: number): Observable<void> {
    return this.http.post<void>(`${this.base}/warehouse/orders/${orderId}/mark-missing`, { orderItemId, missingQty });
  }

  markPacked(orderId: number): Observable<void> {
    return this.http.post<void>(`${this.base}/warehouse/orders/${orderId}/packed`, {});
  }

  riderBatches(status = 'ACTIVE'): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/rider/batches?status=${status}`);
  }

  riderAccept(orderId: number): Observable<void> {
    return this.http.post<void>(`${this.base}/rider/orders/${orderId}/accept`, {});
  }

  riderStatus(orderId: number, payload: unknown): Observable<void> {
    return this.http.post<void>(`${this.base}/rider/orders/${orderId}/status`, payload);
  }

  notifications(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/notifications`);
  }

  markNotificationRead(id: number): Observable<void> {
    return this.http.post<void>(`${this.base}/notifications/${id}/read`, {});
  }

  seedMap(): Observable<any> {
    return this.http.get<any>(`${this.base}/dev/seed-map`);
  }

  orderStream(orderId: number): EventSource {
    return new EventSource(`${this.base}/stream/orders/${orderId}`);
  }
}
