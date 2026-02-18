import { Routes } from '@angular/router';
import { RestaurantListComponent } from './features/restaurants/restaurant-list.component';
import { RestaurantDetailComponent } from './features/restaurants/restaurant-detail.component';
import { CartComponent } from './features/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout.component';
import { OrderListComponent } from './features/orders/order-list.component';
import { OrderDetailComponent } from './features/orders/order-detail.component';
import { OwnerOrdersComponent } from './features/owner/owner-orders.component';
import { OwnerRestaurantsComponent } from './features/owner/owner-restaurants.component';
import { DeliveryJobsComponent } from './features/delivery/delivery-jobs.component';
import { DeliveryOrderComponent } from './features/delivery/delivery-order.component';
import { LoginComponent } from './features/auth/login.component';
import { AddressesComponent } from './features/customer/addresses.component';
import { FavoritesComponent } from './features/customer/favorites.component';
import { AdminRestaurantsComponent } from './features/admin/admin-restaurants.component';
import { AdminReviewsComponent } from './features/admin/admin-reviews.component';
import { adminGuard, customerGuard, deliveryGuard, ownerGuard } from './core/role.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: 'login', component: LoginComponent },

  { path: 'restaurants', component: RestaurantListComponent, canActivate: [customerGuard] },
  { path: 'restaurants/:id', component: RestaurantDetailComponent, canActivate: [customerGuard] },
  { path: 'cart', component: CartComponent, canActivate: [customerGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [customerGuard] },
  { path: 'orders', component: OrderListComponent, canActivate: [customerGuard] },
  { path: 'orders/:id', component: OrderDetailComponent, canActivate: [customerGuard] },
  { path: 'customer/addresses', component: AddressesComponent, canActivate: [customerGuard] },
  { path: 'customer/favorites', component: FavoritesComponent, canActivate: [customerGuard] },

  { path: 'owner/restaurants', component: OwnerRestaurantsComponent, canActivate: [ownerGuard] },
  { path: 'owner/orders', component: OwnerOrdersComponent, canActivate: [ownerGuard] },

  { path: 'delivery/jobs', component: DeliveryJobsComponent, canActivate: [deliveryGuard] },
  { path: 'delivery/order/:id', component: DeliveryOrderComponent, canActivate: [deliveryGuard] },

  { path: 'admin/restaurants', component: AdminRestaurantsComponent, canActivate: [adminGuard] },
  { path: 'admin/reviews', component: AdminReviewsComponent, canActivate: [adminGuard] },

  { path: '**', redirectTo: 'login' },
];
