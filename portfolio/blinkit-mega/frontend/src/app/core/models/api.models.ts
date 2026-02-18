export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface StoreResolveResponse {
  id: number;
  name: string;
  pincode: string;
}

export interface ProductView {
  id: number;
  categoryId: number;
  name: string;
  description: string;
  price: number;
}

export interface CartItemView {
  productId: number;
  productName: string;
  qty: number;
  price: number;
  lineTotal: number;
}

export interface CartView {
  storeId: number;
  items: CartItemView[];
  subtotal: number;
}

export interface CheckoutPreviewResponse {
  reservationId: number;
  expiresAt: string;
  subtotal: number;
  discount: number;
  deliveryFee: number;
  total: number;
}

export interface OrderItemView {
  productId: number;
  productName: string;
  qty: number;
  price: number;
  refundedQty: number;
}

export interface OrderView {
  id: number;
  status: string;
  totalAmount: number;
  createdAt: string;
  items: OrderItemView[];
}
