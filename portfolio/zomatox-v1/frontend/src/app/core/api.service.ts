import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Address, Cart, MenuItem, Order, Restaurant } from './models';

const API = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  restaurants(params: any) { return this.http.get<any>(`${API}/restaurants`, { params }); }
  restaurant(id: number) { return this.http.get<Restaurant>(`${API}/restaurants/${id}`); }
  menu(restaurantId: number) { return this.http.get<MenuItem[]>(`${API}/restaurants/${restaurantId}/menu`); }

  getCart() { return this.http.get<Cart>(`${API}/cart`); }
  upsertCartItem(menuItemId: number, qty: number) { return this.http.put<Cart>(`${API}/cart/items`, { menuItemId, qty }); }
  removeCartItem(menuItemId: number) { return this.http.delete<Cart>(`${API}/cart/items/${menuItemId}`); }

  createOrder(addressId: number, couponCode?: string) { return this.http.post<Order>(`${API}/orders`, { addressId, couponCode }); }
  orders() { return this.http.get<Order[]>(`${API}/orders`); }
  order(id: number) { return this.http.get<Order>(`${API}/orders/${id}`); }

  myAddresses() { return this.http.get<Address[]>(`${API}/addresses`); }
  createAddress(body: any) { return this.http.post<Address>(`${API}/addresses`, body); }
  updateAddress(id: number, body: any) { return this.http.put<Address>(`${API}/addresses/${id}`, body); }
  deleteAddress(id: number) { return this.http.delete<void>(`${API}/addresses/${id}`); }

  confirmPayment(orderId: number, result: 'SUCCESS' | 'FAIL') {
    return this.http.post<any>(`${API}/payments/${orderId}/confirm`, null, { params: { result } });
  }

  applyCoupon(couponCode: string, restaurantId: number) {
    return this.http.post<any>(`${API}/checkout/apply-coupon`, { couponCode, restaurantId });
  }

  orderEvents(orderId: number) { return this.http.get<any[]>(`${API}/orders/${orderId}/events`); }

  restaurantReviews(restaurantId: number, page = 0) {
    return this.http.get<any>(`${API}/restaurants/${restaurantId}/reviews`, { params: { page } });
  }

  postReview(restaurantId: number, orderId: number, rating: number, comment: string) {
    return this.http.post<any>(`${API}/restaurants/${restaurantId}/reviews`, { orderId, rating, comment });
  }

  ownerRestaurants() { return this.http.get<any[]>(`${API}/owner/restaurants`); }
  ownerOrders(status?: string) { return this.http.get<any[]>(`${API}/owner/orders`, { params: status ? { status } : {} as any }); }
  ownerSetOrderStatus(orderId: number, next: string) { return this.http.post<any>(`${API}/owner/orders/${orderId}/status`, null, { params: { next } }); }

  deliveryJobs(status: 'AVAILABLE' | 'ASSIGNED') { return this.http.get<any[]>(`${API}/delivery/jobs`, { params: { status } }); }
  deliveryAccept(orderId: number) { return this.http.post<any>(`${API}/delivery/jobs/${orderId}/accept`, null); }
  deliverySetStatus(orderId: number, next: string) { return this.http.post<any>(`${API}/delivery/orders/${orderId}/status`, null, { params: { next } }); }

  favorites() { return this.http.get<Restaurant[]>(`${API}/favorites/restaurants`); }
  addFavorite(restaurantId: number) { return this.http.post<void>(`${API}/favorites/restaurants/${restaurantId}`, {}); }
  removeFavorite(restaurantId: number) { return this.http.delete<void>(`${API}/favorites/restaurants/${restaurantId}`); }
  addRecent(restaurantId: number) { return this.http.post<void>(`${API}/recent/restaurants/${restaurantId}`, {}); }
  recents(limit = 10) { return this.http.get<Restaurant[]>(`${API}/recent/restaurants`, { params: { limit } }); }

  adminRestaurants(status: string) { return this.http.get<Restaurant[]>(`${API}/admin/restaurants`, { params: { status } }); }
  adminApproveRestaurant(id: number) { return this.http.post<Restaurant>(`${API}/admin/restaurants/${id}/approve`, {}); }
  adminRejectRestaurant(id: number) { return this.http.post<Restaurant>(`${API}/admin/restaurants/${id}/reject`, {}); }
  adminBlockRestaurant(id: number) { return this.http.post<Restaurant>(`${API}/admin/restaurants/${id}/block`, {}); }
  adminUnblockRestaurant(id: number) { return this.http.post<Restaurant>(`${API}/admin/restaurants/${id}/unblock`, {}); }
  adminReviews(status: string) { return this.http.get<any[]>(`${API}/admin/reviews`, { params: { status } }); }
  adminHideReview(id: number) { return this.http.post<any>(`${API}/admin/reviews/${id}/hide`, {}); }
  adminUnhideReview(id: number) { return this.http.post<any>(`${API}/admin/reviews/${id}/unhide`, {}); }
}
