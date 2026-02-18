import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = 'http://localhost:8080/api';

  constructor(private readonly http: HttpClient) {}

  login(payload: unknown): Observable<unknown> {
    return this.http.post(`${this.base}/auth/login`, payload);
  }

  resolveStore(pincode: string): Observable<unknown> {
    return this.http.get(`${this.base}/stores/resolve?pincode=${pincode}`);
  }

  addCartItem(payload: unknown): Observable<unknown> {
    return this.http.put(`${this.base}/cart/items`, payload);
  }

  checkoutPreview(payload: unknown): Observable<unknown> {
    return this.http.post(`${this.base}/checkout/preview`, payload);
  }

  placeOrder(payload: unknown, idemKey: string): Observable<unknown> {
    return this.http.post(`${this.base}/orders`, payload, {
      headers: new HttpHeaders({ 'Idempotency-Key': idemKey })
    });
  }

  markPacked(orderId: number): Observable<unknown> {
    return this.http.post(`${this.base}/warehouse/orders/${orderId}/packed`, {});
  }

  riderStatus(orderId: number, payload: unknown): Observable<unknown> {
    return this.http.post(`${this.base}/rider/orders/${orderId}/status`, payload);
  }

  orderStream(orderId: number): EventSource {
    return new EventSource(`${this.base}/stream/orders/${orderId}`);
  }
}
