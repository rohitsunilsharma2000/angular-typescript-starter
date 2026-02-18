import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list.component';
import { RestaurantDetailComponent } from './features/restaurants/restaurant-detail.component';
import { CartComponent } from './features/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout.component';
import { OrderListComponent } from './features/orders/order-list.component';
import { OrderDetailComponent } from './features/orders/order-detail.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'restaurants' },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'restaurants/:id', component: RestaurantDetailComponent },
  { path: 'cart', component: CartComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'orders', component: OrderListComponent },
  { path: 'orders/:id', component: OrderDetailComponent },
];
