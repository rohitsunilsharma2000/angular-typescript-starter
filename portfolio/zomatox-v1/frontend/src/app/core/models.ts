export type Restaurant = {
  id: number;
  name: string;
  city: string;
  cuisineType: string;
  ratingAvg: number;
  deliveryTimeMin: number;
  imageUrl?: string;
};

export type MenuItem = {
  id: number;
  restaurantId: number;
  name: string;
  price: number;
  isVeg: boolean;
  available: boolean;
  stockQty: number;
};

export type CartLine = {
  menuItemId: number;
  name: string;
  price: number;
  qty: number;
  lineTotal: number;
};

export type Cart = {
  cartId: number;
  userId: number;
  restaurantId: number | null;
  items: CartLine[];
  itemTotal: number;
};

export type OrderItem = { name: string; price: number; qty: number; lineTotal: number; };
export type Order = {
  id: number;
  userId: number;
  restaurantId: number;
  status: string;
  itemTotal: number;
  deliveryFee: number;
  payableTotal: number;
  createdAt: string;
  items: OrderItem[];
};
